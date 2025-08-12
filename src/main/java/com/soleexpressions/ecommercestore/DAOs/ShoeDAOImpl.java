package com.soleexpressions.ecommercestore.DAOs;

import com.soleexpressions.ecommercestore.POJOs.Shoe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShoeDAOImpl implements ShoeDAO {

    private final DBConnector dbConnector = new DBConnector();
    private static final Logger LOGGER = Logger.getLogger(ShoeDAOImpl.class.getName());

    @Override
    public List<Shoe> getAllShoes() throws Exception {
        List<Shoe> shoes = new ArrayList<>();
        String query = "SELECT id, model, base_cost, available_sizes FROM Shoes";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Shoe shoe = new Shoe();
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
                shoes.add(shoe);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching all shoes. With message:", e);
            throw e;
        }
        return shoes;
    }

    @Override
    public Shoe getShoeById(int id) throws Exception {
        Shoe shoe = null;
        String query = "SELECT id, model, base_cost, available_sizes FROM Shoes WHERE id = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
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
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching shoe by ID. With message:", e);
            throw e;
        }
        return shoe;
    }
}
