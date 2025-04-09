package com.mycompany.fumigacionesr;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;

@WebServlet("/verificarLogin")
public class VerificarLoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String usuario = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");

        boolean accesoValido = false;

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=fumigaciones;encrypt=true;trustServerCertificate=true",
                "fumires", "fumires"
            );

            String sql = "SELECT * FROM UsuariosAdmin WHERE usuario = ? AND contrasena = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario);
            stmt.setString(2, contrasena);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                accesoValido = true;
            }

            rs.close();
            stmt.close();
            conn.close();

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
}
