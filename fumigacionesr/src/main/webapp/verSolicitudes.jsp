<%@ page import="java.sql.*, java.util.*, java.io.InputStream" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    int offset = 0;
    int pageSize = 20;

    String offsetParam = request.getParameter("offset");
    if (offsetParam != null) {
        offset = Integer.parseInt(offsetParam);
    }

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
        // Cargar archivo de configuración
        Properties properties = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
        if (inputStream == null) {
            throw new RuntimeException("El archivo config.properties no se pudo encontrar.");
        }
        properties.load(inputStream);

        // Obtener los valores de la conexión desde el archivo config.properties
        String dbUrl = properties.getProperty("db.url");
        String dbUsername = properties.getProperty("db.username");
        String dbPassword = properties.getProperty("db.password");

        // Establecer la conexión a la base de datos
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

        stmt = conn.prepareStatement("SELECT * FROM cotizaciones ORDER BY fecha_solicitud DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        stmt.setInt(1, offset);
        stmt.setInt(2, pageSize);

        rs = stmt.executeQuery();
%>

<html>
<head>
    <title>Solicitudes</title>
    <style>
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        select { padding: 5px; }
        button { padding: 10px 15px; margin: 10px 5px 0 0; }
    </style>
</head>
<body>

<h2>Listado de Solicitudes</h2>

<%
    String successParam = request.getParameter("success");
    if ("true".equals(successParam)) {
%>
    <div id="mensajeExito" style="background-color: #d4edda; color: #155724; padding: 10px; border: 1px solid #c3e6cb; border-radius: 5px; margin-bottom: 20px;">
        ✔ Cambios guardados exitosamente.
    </div>
<%
    }
%>

<a href="panelAdmin.jsp">
    <button>Regresar a inicio</button>
</a>

<form action="GuardarCambiosEstadoServlet" method="post">
    <table>
        <tr>
            <th>Nombre</th>
            <th>Correo</th>
            <th>Servicio</th>
            <th>Fecha</th>
            <th>Estatus</th>
            <th>Costo Estimado</th>
            <th>Teléfono</th> <!-- Nueva columna para el número de teléfono -->
            <th>Ubicación</th> <!-- Nueva columna para la ubicación -->
        </tr>

        <%
            while (rs.next()) {
                int id = rs.getInt("id");
                double costoEstimado = 0;

                PreparedStatement stmtCosto = conn.prepareStatement("SELECT * FROM costos");
                ResultSet rsCosto = stmtCosto.executeQuery();
                if (rsCosto.next()) {
                    double metroCuadrado = rsCosto.getDouble("metroCuadrado");
                    double servicio = rsCosto.getDouble("servicio");

                    String ubicacion = rs.getString("ubicacion");

                    PreparedStatement stmtUbicacionCosto = conn.prepareStatement("SELECT " + ubicacion + " FROM costos");
                    ResultSet rsUbicacionCosto = stmtUbicacionCosto.executeQuery();
                    double ubicacionCosto = 0;
                    if (rsUbicacionCosto.next()) {
                        ubicacionCosto = rsUbicacionCosto.getDouble(1);
                    }

                    double tamaño = rs.getDouble("tamano");
                    costoEstimado = (tamaño * metroCuadrado) + servicio + ubicacionCosto;
                }

                PreparedStatement stmtUpdate = conn.prepareStatement("UPDATE cotizaciones SET costoEstimado = ? WHERE id = ?");
                stmtUpdate.setDouble(1, costoEstimado);
                stmtUpdate.setInt(2, id);
                stmtUpdate.executeUpdate();
        %>
        <tr>
            <td><%= rs.getString("nombre") %> <%= rs.getString("apellidos") %></td>
            <td><%= rs.getString("correo") %></td>
            <td><%= rs.getString("servicio") %></td>
            <td><%= rs.getTimestamp("fecha_solicitud") %></td>
            <td>
                <select name="estado_<%= id %>">
                    <option value="Nueva" <%= rs.getString("estado").equals("Nueva") ? "selected" : "" %>>Nueva</option>
                    <option value="Pendiente" <%= rs.getString("estado").equals("Pendiente") ? "selected" : "" %>>Pendiente</option>
                    <option value="Completada" <%= rs.getString("estado").equals("Completada") ? "selected" : "" %>>Completada</option>
                </select>
            </td>
            <td><%= costoEstimado %></td>
            <td><%= rs.getString("telefono") %></td> <!-- Mostrar el teléfono aquí -->
            <td><%= rs.getString("ubicacion") %></td> <!-- Mostrar la ubicación aquí -->
        </tr>
        <% } %>
    </table>

    <button type="submit">Guardar Cambios</button>
</form>

<form method="get" action="verSolicitudes.jsp">
    <input type="hidden" name="offset" value="<%= offset + pageSize %>" />
    <button type="submit">Cargar más</button>
</form>

<script>
    window.onload = function() {
        const mensaje = document.getElementById("mensajeExito");
        if (mensaje) {
            setTimeout(() => {
                mensaje.style.transition = "opacity 1s ease";
                mensaje.style.opacity = "0";
                setTimeout(() => mensaje.remove(), 1000);
            }, 3000);
        }
    };
</script>

</body>
</html>

<%
    } catch (Exception e) {
        out.println("Error: " + e.getMessage());
    } finally {
        if (rs != null) rs.close();
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
    }
%>
