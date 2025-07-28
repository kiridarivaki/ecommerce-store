package com.soleexpressions.ecommercestore.DAOs;

import com.soleexpressions.ecommercestore.POJOs.Shoe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShoeDAOImpl implements ShoeDAO {

    private final DBConnector dbConnector = new DBConnector();

    @Override
    public List<Shoe> getAllShoes() {
        List<Shoe> shoes = new ArrayList<>();
        String sql = "SELECT id, model, base_cost, available_sizes FROM Shoes";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Shoe shoe = new Shoe();
                shoe.setId(rs.getInt("id"));
                shoe.setModel(rs.getString("model"));
                shoe.setBaseCost(rs.getDouble("base_cost"));

                // Assuming availableSizes is stored as a comma-separated string in DB
                String sizesString = rs.getString("available_sizes");
                if (sizesString != null && !sizesString.isEmpty()) {
                    List<String> sizes = Arrays.asList(sizesString.split(","));
                    shoe.setAvailableSizes(sizes);
                } else {
                    shoe.setAvailableSizes(new ArrayList<>());
                }
                shoes.add(shoe);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all shoes: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return shoes;
    }

    @Override
    public Shoe getShoeById(int id) {
        Shoe shoe = null;
        String sql = "SELECT id, model, base_cost, available_sizes FROM Shoes WHERE id = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    shoe = new Shoe();
                    shoe.setId(rs.getInt("id"));
                    shoe.setModel(rs.getString("model"));
                    shoe.setBaseCost(rs.getDouble("base_cost"));

                    String sizesString = rs.getString("available_sizes");
                    if (sizesString != null && !sizesString.isEmpty()) {
                        List<String> sizes = Arrays.asList(sizesString.split(","));
                        shoe.setAvailableSizes(sizes);
                    } else {
                        shoe.setAvailableSizes(new ArrayList<>());
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching shoe by ID: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return shoe;
    }
}
