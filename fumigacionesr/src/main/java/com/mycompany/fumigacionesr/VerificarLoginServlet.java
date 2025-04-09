package com.mycompany.fumigacionesr;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.io.InputStream;

@WebServlet("/verificarLogin")
public class VerificarLoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String usuario = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");

        boolean accesoValido = false;

        try (Connection conn = obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM UsuariosAdmin WHERE usuario = ? AND contrasena = ?")) {

            stmt.setString(1, usuario);
            stmt.setString(2, contrasena);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    accesoValido = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (accesoValido) {
            // Usuario autenticado: redirige al panel admin
            response.sendRedirect(request.getContextPath() + "/panelAdmin.jsp");
        } else {
            // Falló el login
            response.sendRedirect(request.getContextPath() + "/admin.html?error=1");
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
