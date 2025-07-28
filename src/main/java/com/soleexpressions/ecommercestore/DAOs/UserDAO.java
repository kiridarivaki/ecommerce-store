package com.soleexpressions.ecommercestore.DAOs;

import com.soleexpressions.ecommercestore.POJOs.ArtistProfile;
import com.soleexpressions.ecommercestore.POJOs.User;

import java.sql.SQLException;

public interface UserDAO {
    public User authenticate(String username, String plainPassword) throws SQLException;

    public boolean doesUsernameExist(String username) throws SQLException;

    public User registerUser(User user, ArtistProfile artistProfile) throws SQLException;

    public User getUserById(int userId) throws SQLException;
}
