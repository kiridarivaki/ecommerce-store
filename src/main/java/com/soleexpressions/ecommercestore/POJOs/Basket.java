package com.soleexpressions.ecommercestore.POJOs;

import com.soleexpressions.ecommercestore.DAOs.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Basket {
    private int id;
    private int userId;
    private LocalDateTime lastUpdatedAt;
    private List<CustomizedShoe> items;

    public Basket() {
        this.items = new ArrayList<>();
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public Basket(int userId, LocalDateTime lastUpdatedAt, List<CustomizedShoe> items) {
        this.userId = userId;
        this.lastUpdatedAt = lastUpdatedAt;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public List<CustomizedShoe> getItems() {
        return items;
    }

    public void setItems(List<CustomizedShoe> items) {
        this.items = items;
    }

    public void addCartItem(CustomizedShoe item) {
        this.items.add(item);
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public double getTotalCost() {
        return items.stream()
                .mapToDouble(CustomizedShoe::getCalculatedTotalCost)
                .sum();
    }

    public void addProduct(String username, int prodid) throws SQLException {
        Connection con = null;
        String sql = "INSERT INTO buys(username, productId ) "
                + " VALUES (?,?);";
        DBConnector db = new DBConnector();
        try {
            con = db.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setInt(2, prodid);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Could not close connection with the Database Server: "
                    + e.getMessage());
        } finally {
            try {
                db.close();
            } catch (Exception e) {
            }
        }
    }


    public void addBasket(Basket basket) throws SQLException {
        Connection con = null;
        String sql = "INSERT INTO basket(basketId, cost, userId) "
                + " VALUES (?,?,?);";
        DBConnector db = new DBConnector();

        try {
            con = db.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, basket.getId());
            stmt.setDouble(2, basket.getTotalCost());
            stmt.setInt(3, basket.getUserId());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Could not close connection with the Database Server: "
                    + e.getMessage());
        } finally {
            try {
                db.close();
            } catch (Exception e) {
            }
        }
    }


    public int countProducts(String username) throws SQLException {
        Connection con = null;
        String sql = "SELECT * FROM Buys WHERE username=?";
        DBConnector db = new DBConnector();
        try {
            int count = 0;
            con = db.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                count++;
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Could not close connection with the Database Server: "
                    + e.getMessage());
        } finally {
            try {
                db.close();
            } catch (Exception e) {
            }
        }
    }
}
