/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.calculadoraimc;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "CalculoIMC", urlPatterns = {"/CalculoIMC"})
public class CalculoIMC extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String nombre = request.getParameter("nombre");
            int edad = Integer.parseInt(request.getParameter("edad"));
            String sexo = request.getParameter("sexo");
            double estatura = Double.parseDouble(request.getParameter("estatura"));
            double peso = Double.parseDouble(request.getParameter("peso"));

            double imc = peso / (estatura * estatura);

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Resultado IMC</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Resultado IMC</h1>");
            out.println("<p>Nombre: " + nombre + "</p>");
            out.println("<p>Edad: " + edad + "</p>");
            out.println("<p>Sexo: " + sexo + "</p>");
            out.println("<p>Estatura: " + estatura + " metros</p>");
            out.println("<p>Peso: " + peso + " kilogramos</p>");
            out.println("<p>IMC: " + imc + "</p>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}