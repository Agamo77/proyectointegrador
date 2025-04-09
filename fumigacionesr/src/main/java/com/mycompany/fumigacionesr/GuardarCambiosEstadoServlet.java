package com.mycompany.fumigacionesr;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.*;
import java.util.Enumeration;
import java.util.Properties;
import java.io.InputStream;

@WebServlet("/GuardarCambiosEstadoServlet")
public class GuardarCambiosEstadoServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Connection conn = null;
        PreparedStatement stmt = null;

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

            // Establecer la conexión con la base de datos
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

            // Recorremos todos los parámetros
            Enumeration<String> paramNames = request.getParameterNames();

            while (paramNames.hasMoreElements()) {
                String param = paramNames.nextElement();

                if (param.startsWith("estado_")) {
                    int id = Integer.parseInt(param.substring(7)); // Extraemos ID
                    String nuevoEstado = request.getParameter(param);

                    // Actualizar en base de datos
                    stmt = conn.prepareStatement("UPDATE cotizaciones SET estado = ? WHERE id = ?");
                    stmt.setString(1, nuevoEstado);
                    stmt.setInt(2, id);
                    stmt.executeUpdate();
                    stmt.close();
                }
            }

            // Redirigir a la misma página
            response.sendRedirect("verSolicitudes.jsp?success=true");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al guardar cambios: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
