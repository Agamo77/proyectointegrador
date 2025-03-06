package com.mycompany.calculadoraimc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "CalculoIMC", urlPatterns = {"/CalculoIMC"})
public class CalculoIMC extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.html");
            return;
        }

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String usuario = (String) session.getAttribute("usuario");
            double peso = Double.parseDouble(request.getParameter("peso"));
            double estatura = 0;
            double imc = 0;

            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            } catch (ClassNotFoundException e) {
                out.println("<!DOCTYPE html><html><head><title>Error</title></head><body><h1>Error</h1><p>Controlador JDBC no encontrado: " + e.getMessage() + "</p></body></html>");
                e.printStackTrace();
                return;
            }

            String url = "jdbc:sqlserver://localhost\\sqlexpress:1433;databaseName=IMC;encrypt=true;trustServerCertificate=true;";
            String user = "imc_connect";
            String pass = "imc_connect";

            try (Connection conn = DriverManager.getConnection(url, user, pass)) {
                PreparedStatement pstmt = conn.prepareStatement("SELECT estatura FROM Usuarios WHERE usuario = ?");
                pstmt.setString(1, usuario);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    estatura = rs.getDouble("estatura");
                    imc = peso / (estatura * estatura);
                    DecimalFormat df = new DecimalFormat("#.##");
                    imc = Double.parseDouble(df.format(imc));

                    // Insertar en HistorialIMC
                    PreparedStatement pstmtHistorial = conn.prepareStatement("INSERT INTO HistorialIMC (usuario, peso, imc) VALUES (?, ?, ?)");
                    pstmtHistorial.setString(1, usuario);
                    pstmtHistorial.setDouble(2, peso);
                    pstmtHistorial.setDouble(3, imc);
                    pstmtHistorial.executeUpdate();
                }

                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<link rel=\"stylesheet\" href=\"estilos.css\">");
                out.println("<title>Resultado IMC</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Resultado IMC</h1>");
                out.println("<p>Peso: " + peso + " kilogramos</p>");
                out.println("<p>Estatura: " + estatura + " metros</p>");
                out.println("<p>IMC: " + imc + "</p>");

                // Mostrar historial
                out.println("<h2>Historial de IMC</h2>");
                out.println("<table border='1'>");
                out.println("<tr><th>Peso</th><th>IMC</th><th>Fecha</th></tr>");

                PreparedStatement pstmtHistorialConsulta = conn.prepareStatement("SELECT peso, imc, fecha FROM HistorialIMC WHERE usuario = ? ORDER BY fecha DESC");
                pstmtHistorialConsulta.setString(1, usuario);
                ResultSet rsHistorial = pstmtHistorialConsulta.executeQuery();

                while (rsHistorial.next()) {
                    out.println("<tr>");
                    out.println("<td>" + rsHistorial.getDouble("peso") + "</td>");
                    out.println("<td>" + rsHistorial.getDouble("imc") + "</td>");
                    out.println("<td>" + rsHistorial.getTimestamp("fecha") + "</td>");
                    out.println("</tr>");
                }

                out.println("</table>");

                // Botón "Calcular Otra Vez"
                out.println("<br><button onclick=\"location.href='calculadora.html'\">Calcular Otra Vez</button>");

                out.println("</body>");
                out.println("</html>");

            } catch (SQLException e) {
                out.println("<!DOCTYPE html><html><head><title>Error</title></head><body><h1>Error</h1><p>Ocurrió un error: " + e.getMessage() + "</p></body></html>");
                e.printStackTrace();
            }
        }
    }
}