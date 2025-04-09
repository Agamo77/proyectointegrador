package com.mycompany.fumigacionesr;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/GuardarCambiosCostosServlet")
public class GuardarCambiosCostosServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Obtener parámetros del formulario
            String metroCuadrado = request.getParameter("metroCuadrado");
            String monterrey = request.getParameter("Monterrey");
            String apodaca = request.getParameter("Apodaca");
            String escobedo = request.getParameter("Escobedo");
            String sanPedro = request.getParameter("SanPedro");
            String servicio = request.getParameter("servicio");

            // Validar que los campos no estén vacíos
            if (metroCuadrado != null && servicio != null &&
                !metroCuadrado.isEmpty() && !servicio.isEmpty()) {

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

                // Crear la consulta SQL para insertar los costos
                String sql = "INSERT INTO costos (metroCuadrado, Monterrey, Apodaca, Escobedo, SanPedro, servicio) VALUES (?, ?, ?, ?, ?, ?)";

                // Preparar la consulta
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, metroCuadrado);
                stmt.setString(2, monterrey);  // Monterrey
                stmt.setString(3, apodaca);    // Apodaca
                stmt.setString(4, escobedo);   // Escobedo
                stmt.setString(5, sanPedro);   // San Pedro
                stmt.setString(6, servicio);

                // Ejecutar la actualización
                int rowsAffected = stmt.executeUpdate();

                // Si la inserción fue exitosa, redirigir con el mensaje de éxito
                if (rowsAffected > 0) {
                    response.sendRedirect("modificarCostos.jsp?success=true");
                } else {
                    // Si no se pudo insertar, redirigir con un mensaje de error
                    response.sendRedirect("modificarCostos.jsp?success=false");
                }

            } else {
                // Si hay campos vacíos, redirigir con un mensaje de error
                response.sendRedirect("modificarCostos.jsp?success=false");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("modificarCostos.jsp?success=false");
        } finally {
            // Cerrar recursos
            if (stmt != null) try { stmt.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
}
