package com.soleexpressions.ecommercestore.DAOs;

import com.soleexpressions.ecommercestore.POJOs.Basket;
import com.soleexpressions.ecommercestore.POJOs.CustomizedShoe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BasketDAOImpl implements BasketDAO {

    private final DBConnector dbConnector;
    private static final Logger LOGGER = Logger.getLogger(BasketDAOImpl.class.getName());

    public BasketDAOImpl() {
        this.dbConnector = new DBConnector();
    }

    @Override
    public Basket getBasketByUserId(int userId) throws Exception {
        String sql = "SELECT * FROM Baskets WHERE user_id = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Basket basket = new Basket();
                basket.setId(rs.getInt("id"));
                basket.setUserId(rs.getInt("user_id"));
                Timestamp lastUpdatedAtTimestamp = rs.getTimestamp("last_updated_at");
                if (lastUpdatedAtTimestamp != null) {
                    basket.setLastUpdatedAt(lastUpdatedAtTimestamp.toLocalDateTime());
                }
                return basket;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting basket by user ID {0}. With message: {1}", new Object[]{userId, e});
            throw e;
        }
        return null;
    }

    @Override
    public Basket createBasket(int userId) throws Exception {
        String sql = "INSERT INTO Baskets (user_id, last_updated_at) VALUES (?, ?) RETURNING id";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Basket basket = new Basket();
                basket.setId(rs.getInt("id"));
                basket.setUserId(userId);
                basket.setLastUpdatedAt(LocalDateTime.now());
                LOGGER.log(Level.INFO, "Created new basket ID {0}.", basket.getId());

                return basket;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating basket for user ID {0}. With message: {1}", new Object[]{userId, e});
            throw e;
        }
        return null;
    }

    @Override
    public void addShoeToBasket(int customizedShoeId, int basketId) throws Exception {
        String sql = "UPDATE Customized_Shoes SET basket_id = ? WHERE id = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, basketId);
            pstmt.setInt(2, customizedShoeId);
            pstmt.executeUpdate();

            LOGGER.log(Level.INFO, "Assigned customized shoe {0} to basket ID {1}.", new Object[]{customizedShoeId, basketId});
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding customized shoe ID {0} to basket. With message: {1}", new Object[]{customizedShoeId, e});
            throw e;
        }
    }

    @Override
    public List<CustomizedShoe> getBasketContents(int basketId) throws Exception {
        List<CustomizedShoe> customizedShoes = new ArrayList<>();
        String sql = "SELECT cs.*, s.model FROM Customized_Shoes cs " +
                "JOIN Shoes s ON cs.shoe_id = s.id " +
                "WHERE cs.basket_id = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, basketId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                CustomizedShoe cs = new CustomizedShoe();
                cs.setId(rs.getInt("id"));
                cs.setShoeId(rs.getInt("shoe_id"));
                cs.setSelectedSize(rs.getString("selected_size"));
                cs.setSelectedBaseColor(rs.getString("selected_base_color"));
                cs.setBaseColorCost(rs.getDouble("base_color_cost"));
                cs.setSelectedSoleColor(rs.getString("selected_sole_color"));
                cs.setSoleColorCost(rs.getDouble("sole_color_cost"));
                cs.setSelectedLaceColor(rs.getString("selected_lace_color"));
                cs.setLaceColorCost(rs.getDouble("lace_color_cost"));
                cs.setArtistId(rs.getInt("artist_id"));
                cs.setDescription(rs.getString("description"));
                cs.setCalculatedTotalCost(rs.getDouble("calculated_total_cost"));
                cs.setBasketId(rs.getInt("basket_id"));
                cs.setShoeName(rs.getString("model"));
                customizedShoes.add(cs);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting customized shoes for basket ID {0}. With message: {1}", new Object[]{basketId, e});
            throw e;
        }
        return customizedShoes;
    }

    @Override
    public void removeShoeFromBasket(int customizedShoeId) throws Exception {
        String sql = "UPDATE Customized_Shoes SET basket_id = NULL WHERE id = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customizedShoeId);
            pstmt.executeUpdate();

            LOGGER.log(Level.INFO, "Removed customized shoe ID {0} from the basket.", customizedShoeId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error removing customized shoe ID {0} from the basket. With message: {1}", new Object[]{customizedShoeId, e});
            throw e;
        }
    }

    @Override
    public void clearBasket(int basketId) throws Exception {
        String sql = "UPDATE Customized_Shoes SET basket_id = NULL WHERE basket_id = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, basketId);
            pstmt.executeUpdate();

            LOGGER.log(Level.INFO, "Cleared basket ID {)}.", basketId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error clearing basket ID {0}. With message: {1}", new Object[]{basketId, e});
            throw e;
        }
    }
}