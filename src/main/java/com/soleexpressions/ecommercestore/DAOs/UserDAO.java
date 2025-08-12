package com.soleexpressions.ecommercestore.DAOs;

import com.soleexpressions.ecommercestore.POJOs.ArtistProfile;
import com.soleexpressions.ecommercestore.POJOs.User;

public interface UserDAO {
    public User authenticate(String username, String plainPassword) throws Exception;

    public boolean usernameExists(String username) throws Exception;

    public User register(User user, ArtistProfile artistProfile) throws Exception;

    public User getUserById(int userId) throws Exception;
}
