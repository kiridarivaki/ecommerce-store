package com.soleexpressions.ecommercestore.DAOs;

import com.soleexpressions.ecommercestore.POJOs.ArtistProfile;
import com.soleexpressions.ecommercestore.POJOs.User;
import com.soleexpressions.ecommercestore.util.PasswordUtil;

import java.sql.*;

public class UserDAOIml implements UserDAO {

    private final DBConnector dbConnector = new DBConnector();

    public UserDAOIml() {
    }

    public User authenticate(String username, String plainPassword) throws SQLException {
        String sql = "SELECT id, username, password_hash, first_name, last_name, email, phone_number, role FROM users WHERE username = ?";
        User user = null;
        try (Connection con = dbConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
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
            throw new RuntimeException(e);
        }
        return user;
    }

    public boolean doesUsernameExist(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection con = dbConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public User registerUser(User user, ArtistProfile artistProfile) throws SQLException {
        Connection con = null;
        try {
            con = dbConnector.getConnection();
            con.setAutoCommit(false);

            String hashedPassword = PasswordUtil.hashPassword(user.getPasswordHash());
            user.setPasswordHash(hashedPassword);

            String userSql = "INSERT INTO users (username, password_hash, first_name, last_name, email, phone_number, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement userStmt = con.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                userStmt.setString(1, user.getUsername());
                userStmt.setString(2, user.getPasswordHash());
                userStmt.setString(3, user.getFirstname());
                userStmt.setString(4, user.getLastname());
                userStmt.setString(5, user.getEmail());
                userStmt.setString(6, user.getPhoneNumber());
                userStmt.setString(7, user.getRole());

                int affectedRows = userStmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating user failed, no rows affected.");
                }

                try (ResultSet generatedKeys = userStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            }

            if ("artist".equalsIgnoreCase(user.getRole()) && artistProfile != null) {
                String artistProfileSql = "INSERT INTO artist_profiles (user_id, style, bio, portfolio_url, facebook_url, instagram_url) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement artistStmt = con.prepareStatement(artistProfileSql)) {
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
            return user;
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT id, username, password_hash, first_name, last_name, email, phone_number, role FROM users WHERE id = ?";
        try (Connection con = dbConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
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
            throw new RuntimeException(e);
        }
        return null;
    }
}