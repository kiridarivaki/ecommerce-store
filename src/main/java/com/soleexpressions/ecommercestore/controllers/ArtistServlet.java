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
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/artists")
public class ArtistServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ArtistServlet.class.getName());
    private ArtistProfileDAO artistProfileDAO;
    private CustomizedShoeDAO customizedShoeDAO;
    private UserDAOIml userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        artistProfileDAO = new ArtistProfileDAOImpl();
        customizedShoeDAO = new CustomizedShoeDAOImpl();
        userDAO = new UserDAOIml();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        String customizationIdParam = request.getParameter("customizationId");
        int customizationId = -1;

        if (customizationIdParam != null && !customizationIdParam.isEmpty()) {
            try {
                customizationId = Integer.parseInt(customizationIdParam);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid customization ID parameter.", e);
            }
        }

        try {
            if (customizationId == -1) {
                session.setAttribute("toastrError", "You must customize a shoe first before selecting an artist.");
                response.sendRedirect(request.getContextPath() + "/customizations"); // Redirect to customization page
                return;
            }

            CustomizedShoe existingShoe = customizedShoeDAO.getCustomizedShoeById(customizationId);
            if (existingShoe == null) {
                session.setAttribute("toastrError", "The specified custom shoe was not found. Please start customization again.");
                response.sendRedirect(request.getContextPath() + "/customizations"); // Redirect to customization page
                return;
            }

            List<ArtistProfile> artistProfiles = artistProfileDAO.getAllArtists();
            for (ArtistProfile profile : artistProfiles) {
                User user = userDAO.getUserById(profile.getUserId());
                profile.setUser(user);
            }
            request.setAttribute("artistProfiles", artistProfiles);
            request.setAttribute("customizationId", customizationId);
            request.getRequestDispatcher("/views/product/artist.jsp").forward(request, response);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error during artist or shoe retrieval.", e);
            session.setAttribute("toastrError", "Error loading artists: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/views/common/error.jsp");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error in doGet.", e);
            session.setAttribute("toastrError", "An unexpected error occurred: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/views/common/error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String selectedArtistIdStr = request.getParameter("selectedArtistId");
        String customizationIdStr = request.getParameter("customizationId");

        try {
            if (customizationIdStr == null || customizationIdStr.isEmpty()) {
                LOGGER.log(Level.WARNING, "Missing customization ID for artist assignment.");
                session.setAttribute("toastrError", "Could not assign artist: Custom shoe ID missing.");
                response.sendRedirect(request.getContextPath() + "/artists");
                return;
            }

            if (selectedArtistIdStr == null || selectedArtistIdStr.isEmpty()) {
                LOGGER.log(Level.WARNING, "No artist selected during form submission.");
                session.setAttribute("toastrWarning", "Please select an artist before proceeding."); // Use warning for this
                response.sendRedirect(request.getContextPath() + "/artists?customizationId=" + customizationIdStr);
                return;
            }

            int artistId = Integer.parseInt(selectedArtistIdStr);
            int customizationId = Integer.parseInt(customizationIdStr);

            CustomizedShoe customizedShoe = customizedShoeDAO.getCustomizedShoeById(customizationId);

            if (customizedShoe != null) {
                customizedShoe.setArtistId(artistId);
                customizedShoeDAO.updateCustomizedShoe(customizedShoe);

                LOGGER.log(Level.INFO, "Customized shoe updated with artist ID.");

                session.setAttribute("toastrSuccess", "Artist assigned and shoe added to basket!");
                response.sendRedirect(request.getContextPath() + "/basket?action=add&customizationId=" + customizationId);

            } else {
                LOGGER.log(Level.WARNING, "Customized shoe not found.");
                session.setAttribute("toastrError", "Custom shoe not found. Please try again.");
                response.sendRedirect(request.getContextPath() + "/artists");
            }

        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid ID format received.", e);
            session.setAttribute("toastrError", "Invalid selection. Please try again.");
            response.sendRedirect(request.getContextPath() + "/artists?customizationId=" + customizationIdStr);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error during artist assignment.", e);
            session.setAttribute("toastrError", "Database error. Could not assign artist or add to basket.");
            response.sendRedirect(request.getContextPath() + "/artists?customizationId=" + customizationIdStr);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error in doPost.", e);
            session.setAttribute("toastrError", "An unexpected error occurred. Please try again.");
            response.sendRedirect(request.getContextPath() + "/artists?customizationId=" + customizationIdStr);
        }
    }
}