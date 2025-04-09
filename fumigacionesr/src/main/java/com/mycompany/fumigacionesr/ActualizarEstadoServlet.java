package com.mycompany.fumigacionesr;

import java.io.IOException;
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

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=fumigaciones;encrypt=true;trustServerCertificate=true",
                "fumires", "fumires"
            );

            PreparedStatement stmt = conn.prepareStatement("UPDATE cotizaciones SET estado = ? WHERE id = ?");
            stmt.setString(1, estado);
            stmt.setInt(2, id);
            stmt.executeUpdate();

            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("verSolicitudes.jsp");
    }
}
