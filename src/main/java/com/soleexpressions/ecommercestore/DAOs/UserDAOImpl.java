package com.soleexpressions.ecommercestore.DAOs;

import com.soleexpressions.ecommercestore.POJOs.ArtistProfile;
import com.soleexpressions.ecommercestore.POJOs.User;
import com.soleexpressions.ecommercestore.util.PasswordUtil;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAOImpl implements UserDAO {

    private final DBConnector dbConnector = new DBConnector();
    private static final Logger LOGGER = Logger.getLogger(UserDAOImpl.class.getName());

    public UserDAOImpl() {
    }

    public User authenticate(String username, String plainPassword) throws Exception {
        String query = "SELECT id, username, password_hash, first_name, last_name, email, phone_number, role FROM users WHERE username = ?";
        User user = null;
        try (Connection con = dbConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password_hash");
                    if (PasswordUtil.verifyPassword(plainPassword, hashedPassword)) {
                        user = new User(
                                rs.getInt("id"),
                                rs.getString("username"),
                                hashedPassword,
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                rs.getString("email"),
                                rs.getString("phone_number"),
                                rs.getString("role")
                        );
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to authenticate user {0}. With message: {1}", new Object[]{username, e});
            throw e;
        }
        return user;
    }

    public boolean usernameExists(String username) throws Exception {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection con = dbConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to check for a user with username {0}. With message: {1}", new Object[]{username, e});
            throw e;
        }
        return false;
    }

    public User register(User user, ArtistProfile artistProfile) throws Exception {
        Connection con = null;
        try {
            con = dbConnector.getConnection();
            con.setAutoCommit(false);

            String hashedPassword = PasswordUtil.hashPassword(user.getPasswordHash());
            user.setPasswordHash(hashedPassword);

            String query = "INSERT INTO users (username, password_hash, first_name, last_name, email, phone_number, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement userStmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                userStmt.setString(1, user.getUsername());
                userStmt.setString(2, user.getPasswordHash());
                userStmt.setString(3, user.getFirstname());
                userStmt.setString(4, user.getLastname());
                userStmt.setString(5, user.getEmail());
                userStmt.setString(6, user.getPhoneNumber());
                userStmt.setString(7, user.getRole());

                try (ResultSet generatedKeys = userStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            }

            if ("artist".equalsIgnoreCase(user.getRole()) && artistProfile != null) {
                String artistQuery = "INSERT INTO artist_profiles (user_id, style, bio, portfolio_url, facebook_url, instagram_url) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement artistStmt = con.prepareStatement(artistQuery)) {
                    artistStmt.setInt(1, user.getId());
                    artistStmt.setString(2, artistProfile.getStyle());
                    artistStmt.setString(3, artistProfile.getBio());
                    artistStmt.setString(4, artistProfile.getPortfolioUrl());
                    artistStmt.setString(5, artistProfile.getFacebook());
                    artistStmt.setString(6, artistProfile.getInstagram());

                    artistStmt.executeUpdate();
                }
            }

            con.commit();
            LOGGER.log(Level.INFO, "Successfully registered user {0}", user.getId());

            return user;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Registration failed for user {0}. With message: {1}", new Object[]{user.getUsername(), e});
            if (con != null)
                con.rollback();
            throw e;
        } finally {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }
    }

    public User getUserById(int userId) throws Exception {
        String query = "SELECT id, username, password_hash, first_name, last_name, email, phone_number, role FROM users WHERE id = ?";
        try (Connection con = dbConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password_hash"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("phone_number"),
                            rs.getString("role")
                    );
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to retrieve user by ID {0}. With message: {1}", new Object[]{userId, e});
            throw e;
        }
        return null;
    }
}