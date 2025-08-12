package com.soleexpressions.ecommercestore.controllers;

import com.soleexpressions.ecommercestore.DAOs.*;
import com.soleexpressions.ecommercestore.POJOs.ArtistProfile;
import com.soleexpressions.ecommercestore.POJOs.CustomizedShoe;
import com.soleexpressions.ecommercestore.POJOs.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/artists")
public class ArtistServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ArtistServlet.class.getName());
    private ArtistProfileDAO artistProfileDAO;
    private CustomizedShoeDAO customizedShoeDAO;
    private UserDAOImpl userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        artistProfileDAO = new ArtistProfileDAOImpl();
        customizedShoeDAO = new CustomizedShoeDAOImpl();
        userDAO = new UserDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("userobject");

        if (currentUser == null) {
            session.setAttribute("toastrError", "You must be logged in to access this page.");
            response.sendRedirect(request.getContextPath() + "/views/auth/login.jsp");

            return;
        }

        String customizationIdParam = request.getParameter("customizationId");
        int customizationId = -1;

        if (customizationIdParam == null || customizationIdParam.isEmpty()) {
            session.setAttribute("toastrError", "Missing or invalid customization ID.");
            response.sendRedirect(request.getContextPath() + "/views/product/preferences.jsp");

            return;
        }

        try {
            customizationId = Integer.parseInt(customizationIdParam);
            if (customizationId == -1) {
                session.setAttribute("toastrError", "You must customize a shoe first before selecting an artist.");
                LOGGER.log(Level.WARNING, "Customization id not set.");
                response.sendRedirect(request.getContextPath() + "/preferences");

                return;
            }

            CustomizedShoe existingShoe = customizedShoeDAO.getCustomizedShoeById(customizationId);
            if (existingShoe == null) {
                session.setAttribute("toastrError", "The specified custom shoe was not found. Please start customization again.");
                LOGGER.log(Level.WARNING, "Shoe with customization id {0} not found.", new Object[]{customizationId});
                response.sendRedirect(request.getContextPath() + "/preferences");

                return;
            }

            List<ArtistProfile> artistProfiles = artistProfileDAO.getAllArtists();
            for (ArtistProfile profile : artistProfiles) {
                User user = userDAO.getUserById(profile.getUserId());
                profile.setUser(user);
            }
            request.setAttribute("artistProfiles", artistProfiles);
            request.setAttribute("customizationId", customizationId);
            LOGGER.log(Level.INFO, "Successfully fetched the available artists.");

            request.getRequestDispatcher("/views/product/artist.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            session.setAttribute("toastrError", "Invalid customization ID format. Please start customization again.");
            LOGGER.log(Level.WARNING, "Invalid number format for customization ID {0}. With message: {1}", new Object[]{customizationIdParam, e});

            response.sendRedirect(request.getContextPath() + "/views/product/preferences.jsp");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading the artists. With message: {0}", new Object[]{e});
            session.setAttribute("toastrError", "Error loading the available artists");

            response.sendRedirect(request.getContextPath() + "/views/common/error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        String customizationIdStr = request.getParameter("customizationId");

        if ("updateArtist".equals(action)) {
            String selectedArtistIdStr = request.getParameter("selectedArtistId");

            try {
                if (customizationIdStr == null || customizationIdStr.isEmpty()) {
                    LOGGER.log(Level.WARNING, "Missing customization ID for artist assignment.");
                    session.setAttribute("toastrError", "Could not assign artist: Custom shoe ID missing.");
                    response.sendRedirect(request.getContextPath() + "/artists");

                    return;
                }

                if (selectedArtistIdStr == null || selectedArtistIdStr.isEmpty()) {
                    LOGGER.log(Level.WARNING, "No artist selected during form submission.");
                    session.setAttribute("toastrWarning", "Please select an artist to save.");
                    response.sendRedirect(request.getContextPath() + "/artists?customizationId=" + customizationIdStr);

                    return;
                }

                int artistId = Integer.parseInt(selectedArtistIdStr);
                int customizationId = Integer.parseInt(customizationIdStr);

                CustomizedShoe customizedShoe = customizedShoeDAO.getCustomizedShoeById(customizationId);

                if (customizedShoe != null) {
                    customizedShoe.setArtistId(artistId);
                    customizedShoeDAO.updateCustomizedShoe(customizedShoe);
                    session.setAttribute("toastrSuccess", "Artist saved successfully! You can now add the shoe to your basket.");
                    LOGGER.log(Level.INFO, "Customized shoe updated with artist ID: " + artistId);

                    response.sendRedirect(request.getContextPath() + "/artists?customizationId=" + customizationId);

                } else {
                    session.setAttribute("toastrError", "Custom shoe not found. Please try again.");
                    LOGGER.log(Level.WARNING, "Customized shoe not found.");

                    response.sendRedirect(request.getContextPath() + "/artists");
                }
            } catch (Exception e) {
                session.setAttribute("toastrError", "Failed to set the artist. Please try again.");
                LOGGER.log(Level.SEVERE, "Failed to set selected artist. With message: {0}", new Object[]{e});

                response.sendRedirect(request.getContextPath() + "/artists?customizationId=" + customizationIdStr);
            }
        } else {
            super.doPost(request, response);
        }
    }
}