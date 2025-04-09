package com.mycompany.fumigacionesr;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.*;

@WebServlet("/ActualizarEstadoServlet")
public class ActualizarEstadoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String estado = request.getParameter("estado");

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

            // Establecer la conexión a la base de datos
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

            stmt = conn.prepareStatement("UPDATE cotizaciones SET estado = ? WHERE id = ?");
            stmt.setString(1, estado);
            stmt.setInt(2, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        response.sendRedirect("verSolicitudes.jsp");
    }
}
