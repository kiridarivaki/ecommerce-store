package com.soleexpressions.ecommercestore.DAOs;

import com.soleexpressions.ecommercestore.POJOs.CustomizedShoe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomizedShoeDAOImpl implements CustomizedShoeDAO {
    private final DBConnector dbConnector = new DBConnector();
    private static final Logger LOGGER = Logger.getLogger(CustomizedShoeDAOImpl.class.getName());

    private CustomizedShoe mapResultSetToCustomizedShoe(ResultSet rs) throws SQLException {
        CustomizedShoe shoe = new CustomizedShoe();
        shoe.setId(rs.getInt("id"));
        shoe.setShoeId(rs.getInt("shoe_id"));
        shoe.setSelectedSize(rs.getString("selected_size"));
        shoe.setSelectedBaseColor(rs.getString("selected_base_color"));
        shoe.setBaseColorCost(rs.getDouble("base_color_cost"));
        shoe.setSelectedSoleColor(rs.getString("selected_sole_color"));
        shoe.setSoleColorCost(rs.getDouble("sole_color_cost"));
        shoe.setSelectedLaceColor(rs.getString("selected_lace_color"));
        shoe.setLaceColorCost(rs.getDouble("lace_color_cost"));
        shoe.setArtistId(rs.getInt("artist_id"));
        shoe.setDescription(rs.getString("description"));
        shoe.setCalculatedTotalCost(rs.getDouble("calculated_total_cost"));

        return shoe;
    }

    @Override
    public CustomizedShoe getCustomizedShoeById(int id) throws Exception {
        String query = "SELECT id, shoe_id, selected_size, selected_base_color, base_color_cost, " +
                "selected_sole_color, sole_color_cost, selected_lace_color, lace_color_cost, " +
                "artist_id, description, calculated_total_cost FROM customized_shoes WHERE id = ?";
        try (Connection con = dbConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCustomizedShoe(rs);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching shoe by ID. With message:", e);
            throw e;
        }
        return null;
    }

    @Override
    public List<CustomizedShoe> getAllCustomizedShoes() throws Exception {
        List<CustomizedShoe> shoes = new ArrayList<>();
        String query = "SELECT id, shoe_id, selected_size, selected_base_color, base_color_cost, " +
                "selected_sole_color, sole_color_cost, selected_lace_color, lace_color_cost, " +
                "artist_id, description, calculated_total_cost FROM customized_shoes";
        try (Connection con = dbConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                shoes.add(mapResultSetToCustomizedShoe(rs));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching all shoes. With message:", e);
            throw e;
        }
        return shoes;
    }

    @Override
    public void addCustomizedShoe(CustomizedShoe shoe) throws Exception {
        String query = "INSERT INTO customized_shoes (shoe_id, selected_size, selected_base_color, base_color_cost, " +
                "selected_sole_color, sole_color_cost, selected_lace_color, lace_color_cost, " +
                "artist_id, description, calculated_total_cost) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = dbConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, shoe.getShoeId());
            stmt.setString(2, shoe.getSelectedSize());
            stmt.setString(3, shoe.getSelectedBaseColor());
            stmt.setDouble(4, shoe.getBaseColorCost());
            stmt.setString(5, shoe.getSelectedSoleColor());
            stmt.setDouble(6, shoe.getSoleColorCost());
            stmt.setString(7, shoe.getSelectedLaceColor());
            stmt.setDouble(8, shoe.getLaceColorCost());
            stmt.setInt(9, shoe.getArtistId());
            stmt.setString(10, shoe.getDescription());
            stmt.setDouble(11, shoe.getCalculatedTotalCost());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Adding customized shoe failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    shoe.setId(generatedKeys.getInt(1));
                    LOGGER.log(Level.INFO, "Successfully saved customized shoe ID {0}", shoe.getId());
                } else {
                    throw new SQLException("Adding customized shoe failed, no ID obtained.");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving customized shoe. With message:", e);
            throw e;
        }
    }

    @Override
    public void updateCustomizedShoe(CustomizedShoe shoe) throws Exception {
        String query = "UPDATE customized_shoes SET shoe_id=?, selected_size=?, selected_base_color=?, base_color_cost=?, " +
                "selected_sole_color=?, sole_color_cost=?, selected_lace_color=?, lace_color_cost=?, " +
                "artist_id=?, description=?, calculated_total_cost=? WHERE id=?";
        try (Connection con = dbConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, shoe.getShoeId());
            stmt.setString(2, shoe.getSelectedSize());
            stmt.setString(3, shoe.getSelectedBaseColor());
            stmt.setDouble(4, shoe.getBaseColorCost());
            stmt.setString(5, shoe.getSelectedSoleColor());
            stmt.setDouble(6, shoe.getSoleColorCost());
            stmt.setString(7, shoe.getSelectedLaceColor());
            stmt.setDouble(8, shoe.getLaceColorCost());
            stmt.setInt(9, shoe.getArtistId());
            stmt.setString(10, shoe.getDescription());
            stmt.setDouble(11, shoe.getCalculatedTotalCost());
            stmt.setInt(12, shoe.getId());
            stmt.executeUpdate();

            LOGGER.log(Level.INFO, "Successfully updated customized shoe ID {0}", shoe.getId());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating customized shoe. With message:", e);
            throw e;
        }
    }

    @Override
    public void deleteCustomizedShoe(int id) throws Exception {
        String query = "DELETE FROM customized_shoes WHERE id = ?";
        try (Connection con = dbConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();

            LOGGER.log(Level.INFO, "Successfully deleted customized shoe ID {0}", id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting customized shoe. With message:", e);
            throw e;
        }
    }
}
