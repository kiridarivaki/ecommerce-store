package com.soleexpressions.ecommercestore.controllers;

import com.google.gson.Gson;
import com.soleexpressions.ecommercestore.DAOs.CustomizedShoeDAO;
import com.soleexpressions.ecommercestore.DAOs.CustomizedShoeDAOImpl;
import com.soleexpressions.ecommercestore.DAOs.ShoeDAO;
import com.soleexpressions.ecommercestore.DAOs.ShoeDAOImpl;
import com.soleexpressions.ecommercestore.POJOs.CustomizedShoe;
import com.soleexpressions.ecommercestore.POJOs.Shoe;
import com.soleexpressions.ecommercestore.POJOs.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NumberFormatException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("userobject");

        if (user == null) {
            session.setAttribute("toastrError", "You must be logged in to access this page.");
            response.sendRedirect(request.getContextPath() + "/views/auth/login.jsp");
            
            return;
        }

        String action = request.getParameter("action");
        if ("getShoeSizes".equals(action)) {
            handleGetShoeSizes(request, response);

            return;
        }

        try {
            List<Shoe> shoes = shoeDAO.getAllShoes();
            request.setAttribute("shoes", shoes);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to retrieve all shoes. With message: {0}", e);
            session.setAttribute("toastrError", "Failed to retrieve shoe models. Please try again later.");
            response.sendRedirect(request.getContextPath() + "/views/product/preferences.jsp");

            return;
        }

        request.getRequestDispatcher("/views/product/preferences.jsp").forward(request, response);
    }

    private void handleGetShoeSizes(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String shoeIdParam = request.getParameter("shoeId");
        List<String> availableSizes = new ArrayList<>();

        if (shoeIdParam == null || shoeIdParam.trim().isEmpty()) {
            response.getWriter().write(gson.toJson(availableSizes));
            return;
        }

        try {
            int shoeId = Integer.parseInt(shoeIdParam.trim());
            Shoe shoe = shoeDAO.getShoeById(shoeId);

            if (shoe != null && shoe.getAvailableSizes() != null) {
                availableSizes = shoe.getAvailableSizes();
            }
            response.getWriter().write(gson.toJson(availableSizes));
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid Shoe ID format {0}. With message: {1} ", new Object[]{shoeIdParam, e});
            response.getWriter().write(gson.toJson(availableSizes));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to retrieve shoe sizes for shoe for ID: {0}. With message: {1}", new Object[]{shoeIdParam, e});
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(Map.of("message", "An unexpected server error occurred.")));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NumberFormatException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("userobject");

        if (user == null) {
            session.setAttribute("toastrError", "You must be logged in to customize a shoe.");
            response.sendRedirect(request.getContextPath() + "/views/auth/login.jsp");
            return;
        }

        int currentUserId = user.getId();

        try {
            String selectedShoeIdParam = request.getParameter("selectedShoeId");
            String selectedSize = request.getParameter("selectedSize");
            String selectedBaseColor = request.getParameter("selectedBaseColor");
            String selectedSoleColor = request.getParameter("selectedSoleColor");
            String selectedLaceColor = request.getParameter("selectedLaceColor");
            String description = request.getParameter("description");
            String calculatedTotalCostParam = request.getParameter("cost");

            if (selectedShoeIdParam == null || selectedShoeIdParam.isEmpty() ||
                    selectedSize == null || selectedSize.isEmpty() ||
                    selectedBaseColor == null || selectedBaseColor.isEmpty() ||
                    selectedSoleColor == null || selectedSoleColor.isEmpty() ||
                    selectedLaceColor == null || selectedLaceColor.isEmpty() ||
                    calculatedTotalCostParam == null || calculatedTotalCostParam.isEmpty()) {
                session.setAttribute("toastrError", "Missing required customization details. Please ensure all options are selected.");
                response.sendRedirect(request.getContextPath() + "/preferences");

                return;
            }

            int shoeId = Integer.parseInt(selectedShoeIdParam);
            double calculatedTotalCost;

            Shoe baseShoe = shoeDAO.getShoeById(shoeId);
            if (baseShoe == null) {
                session.setAttribute("toastrError", "Selected shoe model not found. Please try again.");
                response.sendRedirect(request.getContextPath() + "/preferences");

                return;
            }

            double shoeCost = baseShoe.getBaseCost();

            double sizeDouble = Double.parseDouble(selectedSize);
            if (sizeDouble >= 39 && sizeDouble <= 41) {
                shoeCost += 50;
            } else if (sizeDouble == 44 || sizeDouble == 45) {
                shoeCost += 40;
            } else if (sizeDouble == 36) {
                shoeCost -= 20;
            } else if (sizeDouble == 37) {
                shoeCost -= 10;
            }

            double baseColorCost = 0;
            if (!(selectedBaseColor.equalsIgnoreCase("#000000")) && !(selectedBaseColor.equalsIgnoreCase("#ffffff"))) {
                baseColorCost = 20;
            }
            shoeCost += baseColorCost;

            double soleColorCost = 0;
            if (!(selectedSoleColor.equalsIgnoreCase("#000000")) && !(selectedSoleColor.equalsIgnoreCase("#ffffff"))) {
                soleColorCost = 15;
            }
            shoeCost += soleColorCost;

            double laceColorCost = 0;
            if (selectedLaceColor.equalsIgnoreCase("yellow") || selectedLaceColor.equalsIgnoreCase("blue") || selectedLaceColor.equalsIgnoreCase("red")) {
                laceColorCost = 12;
            } else if (selectedLaceColor.equalsIgnoreCase("pink") || selectedLaceColor.equalsIgnoreCase("purple") || selectedLaceColor.equalsIgnoreCase("orange")) {
                laceColorCost = 15;
            }
            shoeCost += laceColorCost;

            calculatedTotalCost = shoeCost;

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
            customizedShoe.setDescription(description != null ? description : "");
            customizedShoe.setCalculatedTotalCost(calculatedTotalCost);

            customizedShoeDAO.addCustomizedShoe(customizedShoe);
            session.setAttribute("toastrSuccess", "Customization saved successfully!");
            LOGGER.log(Level.INFO, "Customization Id {0} saved successfully.", customizedShoe.getId());

            response.sendRedirect(request.getContextPath() + "/artists?customizationId=" + customizedShoe.getId());
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid number format in request parameters during customization. With message: {0}", e);
            session.setAttribute("toastrError", "Invalid input for shoe customization. Please ensure all fields are correctly selected.");

            response.sendRedirect(request.getContextPath() + "/preferences");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An unexpected error occurred during shoe customization. With message: {0}", e);
            session.setAttribute("toastrError", "An unexpected error occurred during customization. Please try again.");

            response.sendRedirect(request.getContextPath() + "/preferences");
        }
    }
}