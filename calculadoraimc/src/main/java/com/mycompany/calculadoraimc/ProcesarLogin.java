package com.mycompany.calculadoraimc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ProcesarLogin", urlPatterns = {"/ProcesarLogin"})
public class ProcesarLogin extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String usuario = request.getParameter("usuario");
            String contrasena = request.getParameter("contrasena");

            // Carga el controlador JDBC
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            } catch (ClassNotFoundException e) {
                out.println("<!DOCTYPE html><html><head><title>Error</title></head><body><h1>Error</h1><p>Controlador JDBC no encontrado: " + e.getMessage() + "</p></body></html>");
                e.printStackTrace();
                return;
            }

            // Conexión a la base de datos
            String url = "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=IMC;encrypt=true;trustServerCertificate=true;";
            String user = "imc_connect"; // Reemplaza con tus credenciales
            String pass = "imc_connect";

            try (Connection conn = DriverManager.getConnection(url, user, pass);
                 PreparedStatement pstmt = conn.prepareStatement("SELECT nombreCompleto FROM Usuarios WHERE usuario = ? AND contrasena = ?")) {

                pstmt.setString(1, usuario);
                pstmt.setString(2, contrasena);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    // Inicio de sesión exitoso
                    HttpSession session = request.getSession();
                    session.setAttribute("usuario", usuario); // Guarda el nombre de usuario en la sesión
                    String nombreCompleto = rs.getString("nombreCompleto");
                    response.sendRedirect("calculadora.html?nombreCompleto=" + nombreCompleto); // Redirige a la página de cálculo de IMC
                } else {
                    // Inicio de sesión fallido
                    out.println("<!DOCTYPE html><html><head><title>Error de inicio de sesión</title></head><body><h1>Error de inicio de sesión</h1><p>El usuario o la contraseña son incorrectos. Por favor, inténtalo de nuevo.</p></body></html>");
                }

            } catch (SQLException e) {
                out.println("<!DOCTYPE html><html><head><title>Error</title></head><body><h1>Error</h1><p>Ocurrió un error: " + e.getMessage() + "</p></body></html>");
                e.printStackTrace();
            }
        }
    }
}