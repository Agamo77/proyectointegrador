package com.mycompany.calculadoraimc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ProcesarRegistro", urlPatterns = {"/ProcesarRegistro"})
public class ProcesarRegistro extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String usuario = request.getParameter("usuario");
            String contrasena = request.getParameter("contrasena");
            String nombreCompleto = request.getParameter("nombreCompleto");
            int edad = Integer.parseInt(request.getParameter("edad"));
            String sexo = request.getParameter("sexo");
            double estatura = Double.parseDouble(request.getParameter("estatura"));

            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            } catch (ClassNotFoundException e) {
                out.println("<!DOCTYPE html><html><head><title>Error</title></head><body><h1>Error</h1><p>Controlador JDBC no encontrado: " + e.getMessage() + "</p></body></html>");
                e.printStackTrace();
                return; // Sale del método si el controlador no se encuentra
            }

            // Conexión a la base de datos
            String url = "jdbc:sqlserver://localhost\\sqlexpress:1433;databaseName=IMC;encrypt=true;trustServerCertificate=true;";
            String user = "imc_connect";
            String pass = "imc_connect";

            try (Connection conn = DriverManager.getConnection(url, user, pass);
                 PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Usuarios (usuario, contrasena, nombreCompleto, edad, sexo, estatura) VALUES (?, ?, ?, ?, ?, ?)")) {

                pstmt.setString(1, usuario);
                pstmt.setString(2, contrasena);
                pstmt.setString(3, nombreCompleto);
                pstmt.setInt(4, edad);
                pstmt.setString(5, sexo);
                pstmt.setDouble(6, estatura);
                pstmt.executeUpdate();

                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<link rel=\"stylesheet\" href=\"estilos.css\">");
                out.println("<title>Registro exitoso</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Registro exitoso</h1>");
                out.println("<p>Usuario: " + usuario + "</p>");
                out.println("</body>");
                out.println("</html>");

            } catch (SQLException e) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<link rel=\"stylesheet\" href=\"estilos.css\">");
                out.println("<title>Error en el registro</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Error en el registro</h1>");
                out.println("<p>Ocurrió un error al registrar el usuario: " + e.getMessage() + "</p>");
                out.println("</body>");
                out.println("</html>");
                e.printStackTrace();
            }
        }
    }
}