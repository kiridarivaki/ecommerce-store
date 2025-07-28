package com.soleexpressions.ecommercestore.controllers;

import com.google.gson.Gson;
import com.soleexpressions.ecommercestore.DAOs.CustomizedShoeDAO;
import com.soleexpressions.ecommercestore.DAOs.CustomizedShoeDAOImpl;
import com.soleexpressions.ecommercestore.DAOs.ShoeDAO;
import com.soleexpressions.ecommercestore.DAOs.ShoeDAOImpl;
import com.soleexpressions.ecommercestore.POJOs.CustomizedShoe;
import com.soleexpressions.ecommercestore.POJOs.Shoe;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/preferences")
public class PreferencesServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(PreferencesServlet.class.getName());

    private ShoeDAO shoeDAO;
    private CustomizedShoeDAO customizedShoeDAO;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        shoeDAO = new ShoeDAOImpl();
        customizedShoeDAO = new CustomizedShoeDAOImpl();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object userObject = session.getAttribute("userobject");

        if (userObject == null) {
            session.setAttribute("errorMessage", "You must be logged in to access the customization page.");
            response.sendRedirect(request.getContextPath() + "/views/auth/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if ("getShoeSizes".equals(action)) {
            handleGetShoeSizes(request, response);
            return;
        }

        List<Shoe> allShoes;
        try {
            allShoes = shoeDAO.getAllShoes();
            request.setAttribute("allShoes", allShoes);
        } finally {
            request.getRequestDispatcher("/views/product/preferences.jsp").forward(request, response);
        }
    }

    private void handleGetShoeSizes(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String shoeIdParam = request.getParameter("shoeId");

        List<String> availableSizes = new ArrayList<>();

        LOGGER.log(Level.INFO, "Received getShoeSizes request. shoeIdParam: '" + shoeIdParam + "'");

        if (shoeIdParam == null || shoeIdParam.trim().isEmpty()) {
            LOGGER.log(Level.INFO, "Shoe ID not provided or empty for getShoeSizes. Returning empty list.");
            response.getWriter().write(gson.toJson(availableSizes));
            return;
        }

        try {
            int shoeId = Integer.parseInt(shoeIdParam.trim());
            Shoe shoe = shoeDAO.getShoeById(shoeId);

            if (shoe != null && shoe.getAvailableSizes() != null) {
                availableSizes = shoe.getAvailableSizes();
            } else {
                LOGGER.log(Level.INFO, "Shoe with ID " + shoeId + " not found or has no available sizes. Returning empty list.");
            }
            response.getWriter().write(gson.toJson(availableSizes));

        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid Shoe ID format for getShoeSizes: '" + shoeIdParam + "'. Returning empty list.", e);
            response.getWriter().write(gson.toJson(availableSizes));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An unexpected error occurred in handleGetShoeSizes for ID: " + shoeIdParam, e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(new ErrorResponse("An unexpected server error occurred.")));
        }
    }

    private static class ErrorResponse {
        String message;

        public ErrorResponse(String message) {
            this.message = message;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object userObject = session.getAttribute("userobject");

        if (userObject == null) {
            session.setAttribute("errorMessage", "You must be logged in to customize a shoe.");
            response.sendRedirect(request.getContextPath() + "/views/auth/login.jsp");
            return;
        }

        int currentUserId = -1;
        try {
            if (userObject instanceof Integer) {
                currentUserId = (Integer) userObject;
            } else if (userObject != null && userObject.getClass().getName().contains("User")) {
                java.lang.reflect.Method getIdMethod = userObject.getClass().getMethod("getId");
                currentUserId = (Integer) getIdMethod.invoke(userObject);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error extracting user ID from session for POST: " + e.getMessage(), e);
            request.setAttribute("errorMessage", "Invalid user session. Please log in again.");
            doGet(request, response);
            return;
        }

        if (currentUserId == -1) {
            request.setAttribute("errorMessage", "Invalid user ID found in session. Please log in again.");
            doGet(request, response);
            return;
        }

        try {
            String selectedShoeIdParam = request.getParameter("selectedShoeId");
            String selectedSize = request.getParameter("selectedSize");
            String selectedBaseColor = request.getParameter("selectedBaseColor");
            String selectedSoleColor = request.getParameter("selectedSoleColor");
            String selectedLaceColor = request.getParameter("selectedLaceColor");
            String calculatedTotalCostParam = request.getParameter("cost");

            if (selectedShoeIdParam == null || selectedShoeIdParam.isEmpty() ||
                    selectedSize == null || selectedSize.isEmpty() ||
                    selectedBaseColor == null || selectedBaseColor.isEmpty() ||
                    selectedSoleColor == null || selectedSoleColor.isEmpty() ||
                    selectedLaceColor == null || selectedLaceColor.isEmpty() ||
                    calculatedTotalCostParam == null || calculatedTotalCostParam.isEmpty()) {
                request.setAttribute("errorMessage", "Missing required customization details. Please ensure all options are selected.");
                doGet(request, response);
                return;
            }

            int shoeId = Integer.parseInt(selectedShoeIdParam);
            double calculatedTotalCost = Double.parseDouble(calculatedTotalCostParam);

            Shoe baseShoe = shoeDAO.getShoeById(shoeId);
            if (baseShoe == null) {
                request.setAttribute("errorMessage", "Selected shoe model not found. Please try again.");
                doGet(request, response);
                return;
            }

            double reCalculatedCost = baseShoe.getBaseCost();

            double sizeDouble = Double.parseDouble(selectedSize);
            if (sizeDouble >= 39 && sizeDouble <= 41) {
                reCalculatedCost += 50;
            } else if (sizeDouble == 44 || sizeDouble == 45) {
                reCalculatedCost += 40;
            } else if (sizeDouble == 36) {
                reCalculatedCost -= 20;
            } else if (sizeDouble == 37) {
                reCalculatedCost -= 10;
            }

            double baseColorCost = 0;
            if (!(selectedBaseColor.equalsIgnoreCase("#000000")) && !(selectedBaseColor.equalsIgnoreCase("#ffffff"))) {
                baseColorCost = 20;
            }
            reCalculatedCost += baseColorCost;

            double soleColorCost = 0;
            if (!(selectedSoleColor.equalsIgnoreCase("#000000")) && !(selectedSoleColor.equalsIgnoreCase("#ffffff"))) {
                soleColorCost = 15;
            }
            reCalculatedCost += soleColorCost;

            double laceColorCost = 0;
            if (selectedLaceColor.equalsIgnoreCase("yellow") || selectedLaceColor.equalsIgnoreCase("blue") || selectedLaceColor.equalsIgnoreCase("red")) {
                laceColorCost = 12;
            } else if (selectedLaceColor.equalsIgnoreCase("pink") || selectedLaceColor.equalsIgnoreCase("purple") || selectedLaceColor.equalsIgnoreCase("orange")) {
                laceColorCost = 15;
            }
            reCalculatedCost += laceColorCost;

            calculatedTotalCost = reCalculatedCost;

            CustomizedShoe customizedShoe = new CustomizedShoe();
            customizedShoe.setShoeId(shoeId);
            customizedShoe.setSelectedSize(selectedSize);
            customizedShoe.setSelectedBaseColor(selectedBaseColor);
            customizedShoe.setBaseColorCost(baseColorCost);
            customizedShoe.setSelectedSoleColor(selectedSoleColor);
            customizedShoe.setSoleColorCost(soleColorCost);
            customizedShoe.setSelectedLaceColor(selectedLaceColor);
            customizedShoe.setLaceColorCost(laceColorCost);
            customizedShoe.setArtistId(currentUserId);
            customizedShoe.setDescription("User custom creation");
            customizedShoe.setCalculatedTotalCost(calculatedTotalCost);

            customizedShoeDAO.addCustomizedShoe(customizedShoe);

            response.sendRedirect(request.getContextPath() + "/artists?customizationId=" + customizedShoe.getId());

        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid number format in POST parameters during customization: " + e.getMessage(), e);
            request.setAttribute("errorMessage", "Invalid input for shoe customization. Please ensure all fields are correctly selected.");
            doGet(request, response);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error: Could not save customized shoe.", e);
            request.setAttribute("errorMessage", "Database error: Could not save your customization. Please try again later.");
            doGet(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An unexpected error occurred during shoe customization POST.", e);
            request.setAttribute("errorMessage", "An unexpected error occurred during customization. Please try again.");
            doGet(request, response);
        }
    }
}