package com.soleexpressions.ecommercestore.POJOs;

public class ArtistProfile {
    private int userId;
    private String style;
    private String bio;
    private String portfolioUrl;
    private String facebook;
    private String instagram;
    private User user;

    public ArtistProfile() {
    }

    public ArtistProfile(String style, String bio, String portfolioUrl, String facebook, String instagram) {
        this.style = style;
        this.bio = bio;
        this.portfolioUrl = portfolioUrl;
        this.facebook = facebook;
        this.instagram = instagram;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }

    public void setPortfolioUrl(String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}