package com.soleexpressions.ecommercestore.DAOs;


import com.soleexpressions.ecommercestore.POJOs.ArtistProfile;

import java.util.List;

public interface ArtistProfileDAO {
    List<ArtistProfile> getAllArtists() throws Exception;

    ArtistProfile getArtistByUserId(int userId) throws Exception;
}
