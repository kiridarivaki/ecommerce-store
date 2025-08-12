package com.soleexpressions.ecommercestore.controllers;

import com.soleexpressions.ecommercestore.DAOs.UserDAOImpl;
import com.soleexpressions.ecommercestore.POJOs.ArtistProfile;
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

@WebServlet("/signUp")
public class SignUpHandlerServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(SignUpHandlerServlet.class.getName());
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
        }

        User newUser = new User(username, password, firstname, lastname, email, phoneNumber, role);

        try {
            if (userDAO.usernameExists(username)) {
                session.setAttribute("toastrError", "Username already taken. Please choose a different one.");
                request.getRequestDispatcher("/views/auth/signup.jsp").forward(request, response);

                return;
            }

            User registeredUser = userDAO.register(newUser, artistProfile);

            if (registeredUser != null) {
                session.setAttribute("userobject", registeredUser);
                session.setAttribute("toastrSuccess", "Registration successful. Welcome!");
                LOGGER.log(Level.INFO, "Registration successful for user {0}.", username);

                response.sendRedirect(request.getContextPath() + "/preferences");
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            session.setAttribute("toastrError", "Registration failed. Please try again.");
            LOGGER.log(Level.WARNING, "Registration failed for user {0}. With message: {1}", new Object[]{username, e});

            request.getRequestDispatcher("/views/auth/signup.jsp").forward(request, response);
        }
    }
}