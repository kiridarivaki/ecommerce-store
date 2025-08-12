package com.soleexpressions.ecommercestore.DAOs;

import com.soleexpressions.ecommercestore.POJOs.ArtistProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArtistProfileDAOImpl implements ArtistProfileDAO {
    private final DBConnector dbConnector = new DBConnector();
    private static final Logger LOGGER = Logger.getLogger(ArtistProfileDAOImpl.class.getName());

    @Override
    public List<ArtistProfile> getAllArtists() throws Exception {
        List<ArtistProfile> artists = new ArrayList<>();
        String sql = "SELECT user_id, style, bio, portfolio_url, facebook_url, instagram_url FROM artist_profiles";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ArtistProfile artist = new ArtistProfile();
                artist.setUserId(rs.getInt("user_id"));
                artist.setStyle(rs.getString("style"));
                artist.setBio(rs.getString("bio"));
                artist.setPortfolioUrl(rs.getString("portfolio_url"));
                artist.setFacebook(rs.getString("facebook_url"));
                artist.setInstagram(rs.getString("instagram_url"));
                artists.add(artist);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching all available artists. With message:", e);
            throw e;
        }
        return artists;
    }

    @Override
    public ArtistProfile getArtistByUserId(int userId) throws Exception {
        ArtistProfile artist = null;
        String sql = "SELECT user_id, style, bio, portfolio_url, facebook_url, instagram_url FROM artist_profiles WHERE user_id = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    artist = new ArtistProfile();
                    artist.setUserId(rs.getInt("user_id"));
                    artist.setStyle(rs.getString("style"));
                    artist.setBio(rs.getString("bio"));
                    artist.setPortfolioUrl(rs.getString("portfolio_url"));
                    artist.setFacebook(rs.getString("facebook_url"));
                    artist.setInstagram(rs.getString("instagram_url"));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching artist profile. With message:", e);
            throw e;
        }
        return artist;
    }
}
