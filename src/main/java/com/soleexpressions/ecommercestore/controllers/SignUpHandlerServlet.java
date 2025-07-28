package com.soleexpressions.ecommercestore.controllers;

import com.soleexpressions.ecommercestore.DAOs.UserDAOIml;
import com.soleexpressions.ecommercestore.POJOs.ArtistProfile;
import com.soleexpressions.ecommercestore.POJOs.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/signUp")
public class SignUpHandlerServlet extends HttpServlet {

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
        String firstname = request.getParameter("firstName");
        String lastname = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");

        String role = request.getParameter("role");
        ArtistProfile artistProfile = null;

        if ("artist".equals(role)) {
            String style = request.getParameter("style");
            String bio = request.getParameter("bio");
            String portfolioUrl = request.getParameter("portfolioUrl");
            String facebook = request.getParameter("facebook");
            String instagram = request.getParameter("instagram");

            artistProfile = new ArtistProfile(style, bio, portfolioUrl, facebook, instagram);
        } else {
            role = "user";
        }

        User newUser = new User(username, password, firstname, lastname, email, phoneNumber, role);

        try {
            if (userDAO.doesUsernameExist(username)) {
                request.setAttribute("errorMessage", "Username already taken. Please choose a different one.");
                request.getRequestDispatcher("/views/auth/signUp.jsp").forward(request, response);
                return;
            }

            User registeredUser = userDAO.registerUser(newUser, artistProfile);

            if (registeredUser != null) {
                HttpSession session = request.getSession();
                session.setAttribute("userobject", registeredUser);
                response.sendRedirect(request.getContextPath() + "/views/product/preferences.jsp");
            } else {
                request.setAttribute("errorMessage", "Registration failed. Please try again.");
                request.getRequestDispatcher("/views/auth/signUp.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error during registration: " + e.getMessage());
            request.getRequestDispatcher("/views/auth/signUp.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An unexpected error occurred during registration: " + e.getMessage());
            request.getRequestDispatcher("/views/auth/signUp.jsp").forward(request, response);
        }
    }
}