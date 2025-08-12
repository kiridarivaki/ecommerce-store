package com.soleexpressions.ecommercestore.controllers;

import com.soleexpressions.ecommercestore.DAOs.UserDAOImpl;
import com.soleexpressions.ecommercestore.POJOs.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/login")
public class LoginHandlerServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LoginHandlerServlet.class.getName());
    private UserDAOImpl userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAOImpl();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html; charset=UTF-8");
        HttpSession session = request.getSession();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            User authenticatedUser = userDAO.authenticate(username, password);

            if (authenticatedUser != null) {
                session.setAttribute("userobject", authenticatedUser);
                LOGGER.log(Level.INFO, "Login successful for user {0}.", username);

                response.sendRedirect(request.getContextPath() + "/preferences");
            } else {
                session.setAttribute("toastrError", "Invalid username or password. Please try again.");
                LOGGER.log(Level.WARNING, "Invalid credentials provided for user {0}.", username);

                response.sendRedirect(request.getContextPath() + "/views/auth/login.jsp");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Login failed for user {0}. With message: {1}", new Object[]{username, e});
            session.setAttribute("toastrError", "An unexpected error occurred during login. Please try again.");

            response.sendRedirect(request.getContextPath() + "/views/auth/login.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/views/auth/login.jsp");
    }
}