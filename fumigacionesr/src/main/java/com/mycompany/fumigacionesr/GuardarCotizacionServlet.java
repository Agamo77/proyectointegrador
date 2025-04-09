package com.mycompany.fumigacionesr;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

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
                "INSERT INTO Cotizaciones (nombre, apellidos, correo, telefono, contacto, servicio, tamano, ubicacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

            stmt.setString(1, nombre);
            stmt.setString(2, apellidos);
            stmt.setString(3, correo);
            stmt.setString(4, telefono);
            stmt.setString(5, contacto);
            stmt.setString(6, servicio);
            stmt.setString(7, tamano);
            stmt.setString(8, ubicacion);

            stmt.executeUpdate();

            // Redirigir a una página de éxito o de vuelta al formulario
            response.sendRedirect("exito.html");

        } catch (SQLException e) {
            throw new ServletException("Error al guardar cotización", e);
        }
    }

    private Connection obtenerConexion() throws SQLException {
        String url = "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=fumigaciones;encrypt=true;trustServerCertificate=true";
        String usuario = "fumires"; // SQL Auth
        String contraseña = "fumires";
        return DriverManager.getConnection(url, usuario, contraseña);
    }
}
