package com.mycompany.fumigacionesr;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.io.InputStream;

@WebServlet("/guardarCotizacion")
public class GuardarCotizacionServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String correo = request.getParameter("correo");
        String telefono = request.getParameter("telefono");
        String contacto = request.getParameter("contacto");
        String servicio = request.getParameter("servicio");
        String tamano = request.getParameter("tamano");
        String ubicacion = request.getParameter("ubicacion");

        try (Connection conn = obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO Cotizaciones (nombre, apellidos, correo, telefono, contacto, servicio, tamano, ubicacion, fecha_solicitud) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            stmt.setString(1, nombre);
            stmt.setString(2, apellidos);
            stmt.setString(3, correo);
            stmt.setString(4, telefono);
            stmt.setString(5, contacto);
            stmt.setString(6, servicio);
            stmt.setString(7, tamano);
            stmt.setString(8, ubicacion);
            
            stmt.setTimestamp(9, java.sql.Timestamp.valueOf(LocalDateTime.now()));

            stmt.executeUpdate();

            // Redirigir a una página de éxito o de vuelta al formulario
            response.sendRedirect("exito.html");

        } catch (SQLException e) {
            throw new ServletException("Error al guardar cotización", e);
        }
    }

    private Connection obtenerConexion() throws SQLException {
        // Cargar archivo de configuración
        Properties properties = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
        if (inputStream == null) {
            throw new RuntimeException("El archivo config.properties no se pudo encontrar.");
        }
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar el archivo config.properties.", e);
        }

        // Obtener los valores de la conexión desde el archivo config.properties
        String dbUrl = properties.getProperty("db.url");
        String dbUsername = properties.getProperty("db.username");
        String dbPassword = properties.getProperty("db.password");

        // Establecer la conexión con la base de datos
        return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
    }
}
