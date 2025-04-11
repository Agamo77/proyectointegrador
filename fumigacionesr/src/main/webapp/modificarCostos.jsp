<%@ page import="java.sql.*, java.util.*, java.io.InputStream" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String successParam = request.getParameter("success");

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

        // Obtener los costos actuales
        stmt = conn.prepareStatement("SELECT * FROM costos");
        rs = stmt.executeQuery();

        // Verificar si existen datos en la tabla
        if (rs.next()) {
%>

<html>
<head>
    <title>Modificar Costos de Referencia</title>
    <style>
        table { width: 50%; margin-top: 20px; border-collapse: collapse; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        button { padding: 10px 15px; margin: 10px 5px 0 0; }
    </style>
</head>
<body>

<h2>Modificar Costos de Referencia</h2>

<%
    if ("true".equals(successParam)) {
%>
    <div id="mensajeExito" style="background-color: #d4edda; color: #155724; padding: 10px; border: 1px solid #c3e6cb; border-radius: 5px; margin-bottom: 20px;">
        ✔ Cambios guardados exitosamente.
    </div>
<%
    }
%>

<form action="GuardarCambiosCostosServlet" method="post">
    <table>
        <tr>
            <th>Metro Cuadrado</th>
            <th>Monterrey</th>
            <th>Apodaca</th>
            <th>Escobedo</th>
            <th>San Pedro</th>
            <th>Servicio</th>
        </tr>
        <tr>
            <td><input type="text" name="metroCuadrado" value="<%= rs.getString("metroCuadrado") %>" /></td>
            <td><input type="text" name="Monterrey" value="<%= rs.getString("Monterrey") %>" /></td>
            <td><input type="text" name="Apodaca" value="<%= rs.getString("Apodaca") %>" /></td>
            <td><input type="text" name="Escobedo" value="<%= rs.getString("Escobedo") %>" /></td>
            <td><input type="text" name="SanPedro" value="<%= rs.getString("SanPedro") %>" /></td>
            <td><input type="text" name="servicio" value="<%= rs.getString("servicio") %>" /></td>
        </tr>
    </table>
    <button type="submit">Guardar Cambios</button>
</form>

<a href="panelAdmin.jsp">
    <button>Regresar a inicio</button>
</a>

<script>
    window.onload = function() {
        const mensaje = document.getElementById("mensajeExito");
        if (mensaje) {
            setTimeout(() => {
                mensaje.style.transition = "opacity 1s ease";
                mensaje.style.opacity = "0";
                setTimeout(() => mensaje.remove(), 1000);
            }, 3000); // Espera 3 segundos antes de ocultar
        }
    };
</script>

</body>
</html>

<%
        } else {
            
%>

<html>
<head>
    <title>Modificar Costos de Referencia</title>
    <style>
        table { width: 50%; margin-top: 20px; border-collapse: collapse; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        button { padding: 10px 15px; margin: 10px 5px 0 0; }
    </style>
</head>
<body>

<h2>Modificar Costos de Referencia</h2>

<%
    if ("true".equals(successParam)) {
%>
    <div id="mensajeExito" style="background-color: #d4edda; color: #155724; padding: 10px; border: 1px solid #c3e6cb; border-radius: 5px; margin-bottom: 20px;">
        ✔ Costos guardados exitosamente.
    </div>
<%
    }
%>

<form action="GuardarCambiosCostosServlet" method="post">
    <table>
        <tr>
            <th>Metro Cuadrado</th>
            <th>Monterrey</th>
            <th>Apodaca</th>
            <th>Escobedo</th>
            <th>San Pedro</th>
            <th>Servicio</th>
        </tr>
        <tr>
            <td><input type="text" name="metroCuadrado" value="" /></td>
            <td><input type="text" name="Monterrey" value="" /></td>
            <td><input type="text" name="Apodaca" value="" /></td>
            <td><input type="text" name="Escobedo" value="" /></td>
            <td><input type="text" name="SanPedro" value="" /></td>
            <td><input type="text" name="servicio" value="" /></td>
        </tr>
    </table>
    <button type="submit">Guardar Cambios</button>
</form>

<a href="panelAdmin.jsp">
    <button>Regresar a inicio</button>
</a>

<script>
    window.onload = function() {
        const mensaje = document.getElementById("mensajeExito");
        if (mensaje) {
            setTimeout(() => {
                mensaje.style.transition = "opacity 1s ease";
                mensaje.style.opacity = "0";
                setTimeout(() => mensaje.remove(), 1000);
            }, 3000); // Espera 3 segundos antes de ocultar
        }
    };
</script>

</body>
</html>

<%
        }
    } catch (Exception e) {
        out.println("Error: " + e.getMessage());
    } finally {
        if (rs != null) rs.close();
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
    }
%>
