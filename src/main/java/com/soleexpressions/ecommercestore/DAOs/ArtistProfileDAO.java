package com.soleexpressions.ecommercestore.DAOs;


import com.soleexpressions.ecommercestore.POJOs.ArtistProfile;

import java.sql.SQLException;
import java.util.List;

public interface ArtistProfileDAO {
    List<ArtistProfile> getAllArtists() throws SQLException;

    ArtistProfile getArtistByUserId(int userId) throws SQLException;
}
