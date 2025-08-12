package com.soleexpressions.ecommercestore.controllers;

import com.soleexpressions.ecommercestore.DAOs.BasketDAO;
import com.soleexpressions.ecommercestore.DAOs.BasketDAOImpl;
import com.soleexpressions.ecommercestore.POJOs.Basket;
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

@WebServlet("/basket")
public class BasketServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(BasketServlet.class.getName());
    private BasketDAO basketDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        basketDAO = new BasketDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("userobject");

        if (user == null) {
            session.setAttribute("toastrError", "You must be logged in to view your basket.");
            response.sendRedirect(request.getContextPath() + "/views/auth/login.jsp");

            return;
        }

        int currentUserId = user.getId();

        String action = request.getParameter("action");
        if ("remove".equals(action)) {
            handleRemoveFromBasket(request, response, currentUserId);
            return;
        } else if ("clear".equals(action)) {
            handleClearBasket(request, response, currentUserId);
            return;
        }

        try {
            Basket userBasket = basketDAO.getBasketByUserId(currentUserId);
            if (userBasket == null) {
                userBasket = basketDAO.createBasket(currentUserId);
                LOGGER.log(Level.INFO, "New basket created for user ID {0}.", new Object[]{currentUserId});
            }

            List<CustomizedShoe> basketItems = basketDAO.getBasketContents(userBasket.getId());
            request.setAttribute("userBasket", userBasket);
            request.setAttribute("basketItems", basketItems);

            request.getRequestDispatcher("/views/product/basket.jsp").forward(request, response);
        } catch (Exception e) {
            session.setAttribute("toastrError", "Error loading your basket: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Load or create a basket failed for user ID {0}. With message: {1}", new Object[]{currentUserId, e});

            response.sendRedirect(request.getContextPath() + "/views/common/error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("userobject");

        if (user == null) {
            session.setAttribute("toastrError", "You must be logged in to modify your basket.");
            response.sendRedirect(request.getContextPath() + "/views/auth/login.jsp");

            return;
        }

        int currentUserId = user.getId();

        String action = request.getParameter("action");

        if ("add".equals(action)) {
            handleAddCustomizedShoeToBasket(request, response, currentUserId);
        } else if ("checkout".equals(action)) {
            handleCheckout(request, response, currentUserId);
        } else {
            session.setAttribute("toastrWarning", "Unknown action requested for basket.");

            response.sendRedirect(request.getContextPath() + "/basket");
        }
    }

    private void handleAddCustomizedShoeToBasket(HttpServletRequest request, HttpServletResponse response, int currentUserId) throws IOException, NumberFormatException {
        HttpSession session = request.getSession();
        String customizationIdParam = request.getParameter("customizationId");

        if (customizationIdParam == null || customizationIdParam.isEmpty()) {
            session.setAttribute("toastrError", "Missing customized shoe ID to add to basket.");
            LOGGER.log(Level.WARNING, "Missing customized shoe ID parameter for user {0}.", currentUserId);
            response.sendRedirect(request.getContextPath() + "/basket");

            return;
        }

        try {
            int customizedShoeId = Integer.parseInt(customizationIdParam);

            Basket userBasket = basketDAO.getBasketByUserId(currentUserId);
            if (userBasket == null) {
                LOGGER.log(Level.INFO, "No existing basket found for user {0}. Creating a new one.", currentUserId);
                userBasket = basketDAO.createBasket(currentUserId);
            }

            if (userBasket == null) {
                session.setAttribute("toastrError", "An error occurred. Could not create or find your basket.");
                LOGGER.log(Level.SEVERE, "Failed to create or find a basket for user {0}.", currentUserId);
                response.sendRedirect(request.getContextPath() + "/basket");

                return;
            }

            basketDAO.addShoeToBasket(customizedShoeId, userBasket.getId());
            session.setAttribute("toastrSuccess", "Custom shoe added to your basket!");
            LOGGER.log(Level.INFO, "Successfully added customized shoe to basket ID {0}.", userBasket.getId());

            response.sendRedirect(request.getContextPath() + "/basket");
        } catch (NumberFormatException e) {
            session.setAttribute("toastrError", "Invalid shoe ID for basket operation.");
            LOGGER.log(Level.WARNING, "Invalid customized shoe ID format for user ID {0}. With message: {1}", new Object[]{currentUserId, e});

            response.sendRedirect(request.getContextPath() + "/basket");
        } catch (Exception e) {
            session.setAttribute("toastrError", "An error occurred while adding to basket.");
            LOGGER.log(Level.SEVERE, "Error adding customized shoe to basket for user ID {0}. With message: {1}", new Object[]{currentUserId, e});

            response.sendRedirect(request.getContextPath() + "/basket");
        }
    }

    private void handleRemoveFromBasket(HttpServletRequest request, HttpServletResponse response, int currentUserId) throws IOException, NumberFormatException {
        HttpSession session = request.getSession();
        String customizedShoeIdParam = request.getParameter("customizedShoeId");

        if (customizedShoeIdParam == null || customizedShoeIdParam.isEmpty()) {
            session.setAttribute("toastrError", "Missing item ID to remove from basket.");
            response.sendRedirect(request.getContextPath() + "/basket");

            return;
        }

        try {
            int customizedShoeId = Integer.parseInt(customizedShoeIdParam);

            Basket userBasket = basketDAO.getBasketByUserId(currentUserId);
            if (userBasket == null) {
                session.setAttribute("toastrWarning", "Your basket was not found.");
                response.sendRedirect(request.getContextPath() + "/basket");

                return;
            }

            basketDAO.removeShoeFromBasket(customizedShoeId);
            session.setAttribute("toastrSuccess", "Item removed from your basket.");
            LOGGER.log(Level.INFO, "Item successfully removed from basket ID {0}.", userBasket.getId());

            response.sendRedirect(request.getContextPath() + "/basket");
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid item ID format {0}. With message: {1}", new Object[]{customizedShoeIdParam, e});
            session.setAttribute("toastrError", "Invalid item ID for basket operation.");

            response.sendRedirect(request.getContextPath() + "/basket");
        } catch (Exception e) {
            session.setAttribute("toastrError", "An error occurred while removing item.");
            LOGGER.log(Level.SEVERE, "Error removing item from basket for user ID {0}. With message: {1}", new Object[]{currentUserId, e});

            response.sendRedirect(request.getContextPath() + "/basket");
        }
    }

    private void handleClearBasket(HttpServletRequest request, HttpServletResponse response, int currentUserId) throws IOException {
        HttpSession session = request.getSession();
        try {
            Basket userBasket = basketDAO.getBasketByUserId(currentUserId);
            if (userBasket == null) {
                session.setAttribute("toastrWarning", "Your basket is already empty.");
                response.sendRedirect(request.getContextPath() + "/basket");

                return;
            }

            basketDAO.clearBasket(userBasket.getId());
            session.setAttribute("toastrSuccess", "Your basket has been cleared!");
            LOGGER.log(Level.INFO, "Basket ID {0} successfully cleared.", userBasket.getId());

            response.sendRedirect(request.getContextPath() + "/basket");
        } catch (Exception e) {
            session.setAttribute("toastrError", "An error occurred while clearing basket. Please try again.");
            LOGGER.log(Level.SEVERE, "Error clearing basket for user ID {0}. With message: {1}", new Object[]{currentUserId, e});

            response.sendRedirect(request.getContextPath() + "/basket");
        }
    }

    private void handleCheckout(HttpServletRequest request, HttpServletResponse response, int currentUserId) throws IOException {
        HttpSession session = request.getSession();
        try {
            Basket userBasket = basketDAO.getBasketByUserId(currentUserId);
            if (userBasket == null || basketDAO.getBasketContents(userBasket.getId()).isEmpty()) {
                session.setAttribute("toastrError", "Your basket is empty. Add items before checking out.");
                response.sendRedirect(request.getContextPath() + "/basket");

                return;
            }

            basketDAO.clearBasket(userBasket.getId());

            session.setAttribute("toastrSuccess", "Order successfully placed! Your basket has been cleared.");
            LOGGER.log(Level.INFO, "Basket ID {0} successfully checked out.", userBasket.getId());

            response.sendRedirect(request.getContextPath() + "/basket");
        } catch (Exception e) {
            session.setAttribute("toastrError", "An error occurred during checkout.");
            LOGGER.log(Level.SEVERE, "Error during checkout for user ID {0}. With message: {1}", new Object[]{currentUserId, e});

            response.sendRedirect(request.getContextPath() + "/basket");
        }
    }
}