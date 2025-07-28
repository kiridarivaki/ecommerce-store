package com.soleexpressions.ecommercestore.controllers;

import com.soleexpressions.ecommercestore.DAOs.UserDAOIml;
import com.soleexpressions.ecommercestore.POJOs.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginHandlerServlet extends HttpServlet {

    private UserDAOIml userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAOIml();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html; charset=UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            User authenticatedUser = userDAO.authenticate(username, password);

            if (authenticatedUser != null) {
                HttpSession session = request.getSession();
                session.setAttribute("userobject", authenticatedUser);

                response.sendRedirect(request.getContextPath() + "/preferences");
            } else {
                request.setAttribute("errorMessage", "Invalid username or password.");
                request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error during login: " + e.getMessage());
            request.getRequestDispatcher(request.getContextPath() + "/views/auth/login.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An unexpected error occurred during login: " + e.getMessage());
            request.getRequestDispatcher(request.getContextPath() + "/views/auth/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/views/auth/login.jsp");
    }
}