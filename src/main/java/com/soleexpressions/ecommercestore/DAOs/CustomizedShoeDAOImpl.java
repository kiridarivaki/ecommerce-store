package com.soleexpressions.ecommercestore.DAOs;

import com.soleexpressions.ecommercestore.POJOs.CustomizedShoe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomizedShoeDAOImpl implements CustomizedShoeDAO {
    private final DBConnector dbConnector = new DBConnector();

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
    public CustomizedShoe getCustomizedShoeById(int id) throws SQLException {
        String sql = "SELECT id, shoe_id, selected_size, selected_base_color, base_color_cost, " +
                "selected_sole_color, sole_color_cost, selected_lace_color, lace_color_cost, " +
                "artist_id, description, calculated_total_cost FROM customized_shoes WHERE id = ?";
        try (Connection con = dbConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCustomizedShoe(rs);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<CustomizedShoe> getAllCustomizedShoes() throws SQLException {
        List<CustomizedShoe> shoes = new ArrayList<>();
        String sql = "SELECT id, shoe_id, selected_size, selected_base_color, base_color_cost, " +
                "selected_sole_color, sole_color_cost, selected_lace_color, lace_color_cost, " +
                "artist_id, description, calculated_total_cost FROM customized_shoes";
        try (Connection con = dbConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                shoes.add(mapResultSetToCustomizedShoe(rs));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return shoes;
    }

    @Override
    public void addCustomizedShoe(CustomizedShoe shoe) throws SQLException {
        String sql = "INSERT INTO customized_shoes (shoe_id, selected_size, selected_base_color, base_color_cost, " +
                "selected_sole_color, sole_color_cost, selected_lace_color, lace_color_cost, " +
                "artist_id, description, calculated_total_cost) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = dbConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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
                } else {
                    throw new SQLException("Adding customized shoe failed, no ID obtained.");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateCustomizedShoe(CustomizedShoe shoe) throws SQLException {
        String sql = "UPDATE customized_shoes SET shoe_id=?, selected_size=?, selected_base_color=?, base_color_cost=?, " +
                "selected_sole_color=?, sole_color_cost=?, selected_lace_color=?, lace_color_cost=?, " +
                "artist_id=?, description=?, calculated_total_cost=? WHERE id=?";
        try (Connection con = dbConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCustomizedShoe(int id) throws SQLException {
        String sql = "DELETE FROM customized_shoes WHERE id = ?";
        try (Connection con = dbConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
