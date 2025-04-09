package com.mycompany.fumigacionesr;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.*;
import java.util.Enumeration;

@WebServlet("/GuardarCambiosEstadoServlet")
public class GuardarCambiosEstadoServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=fumigaciones;encrypt=true;trustServerCertificate=true",
                "fumires", "fumires"
            );

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
