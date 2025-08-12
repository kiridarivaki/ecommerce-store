package com.soleexpressions.ecommercestore.POJOs;

public class CustomizedShoe {
    private int id;
    private int shoeId;
    private String selectedSize;
    private String selectedBaseColor;
    private double baseColorCost;
    private String selectedSoleColor;
    private double soleColorCost;
    private String selectedLaceColor;
    private double laceColorCost;

    private int artistId;
    private int basketId;
    private String description;
    private double calculatedTotalCost;

    private String shoeName;

    public CustomizedShoe() {
    }

    public CustomizedShoe(int shoeId, String selectedSize,
                          String selectedBaseColor, double baseColorCost,
                          String selectedSoleColor, double soleColorCost,
                          String selectedLaceColor, double laceColorCost,
                          String description) {
        this.shoeId = shoeId;
        this.selectedSize = selectedSize;
        this.selectedBaseColor = selectedBaseColor;
        this.baseColorCost = baseColorCost;
        this.selectedSoleColor = selectedSoleColor;
        this.soleColorCost = soleColorCost;
        this.selectedLaceColor = selectedLaceColor;
        this.laceColorCost = laceColorCost;
        this.description = description;
        this.calculatedTotalCost = 0.0;
    }

    public CustomizedShoe(int id, int shoeId, String selectedSize,
                          String selectedBaseColor, double baseColorCost,
                          String selectedSoleColor, double soleColorCost,
                          String selectedLaceColor, double laceColorCost,
                          String description, double calculatedTotalCost) {
        this.id = id;
        this.shoeId = shoeId;
        this.selectedSize = selectedSize;
        this.selectedBaseColor = selectedBaseColor;
        this.baseColorCost = baseColorCost;
        this.selectedSoleColor = selectedSoleColor;
        this.soleColorCost = soleColorCost;
        this.selectedLaceColor = selectedLaceColor;
        this.laceColorCost = laceColorCost;
        this.description = description;
        this.calculatedTotalCost = 0.0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShoeId() {
        return shoeId;
    }

    public void setShoeId(int shoeId) {
        this.shoeId = shoeId;
    }

    public String getSelectedSize() {
        return selectedSize;
    }

    public void setSelectedSize(String selectedSize) {
        this.selectedSize = selectedSize;
    }

    public String getSelectedBaseColor() {
        return selectedBaseColor;
    }

    public void setSelectedBaseColor(String selectedBaseColor) {
        this.selectedBaseColor = selectedBaseColor;
    }

    public double getBaseColorCost() {
        return baseColorCost;
    }

    public void setBaseColorCost(double baseColorCost) {
        this.baseColorCost = baseColorCost;
    }

    public String getSelectedSoleColor() {
        return selectedSoleColor;
    }

    public void setSelectedSoleColor(String selectedSoleColor) {
        this.selectedSoleColor = selectedSoleColor;
    }

    public double getSoleColorCost() {
        return soleColorCost;
    }

    public void setSoleColorCost(double soleColorCost) {
        this.soleColorCost = soleColorCost;
    }

    public String getSelectedLaceColor() {
        return selectedLaceColor;
    }

    public void setSelectedLaceColor(String selectedLaceColor) {
        this.selectedLaceColor = selectedLaceColor;
    }

    public double getLaceColorCost() {
        return laceColorCost;
    }

    public void setLaceColorCost(double laceColorCost) {
        this.laceColorCost = laceColorCost;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public int getBasketId() {
        return basketId;
    }

    public void setBasketId(int basketId) {
        this.basketId = basketId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCalculatedTotalCost() {
        return calculatedTotalCost;
    }

    public void setCalculatedTotalCost(double calculatedTotalCost) {
        this.calculatedTotalCost = calculatedTotalCost;
    }

    public String getShoeName() {
        return shoeName;
    }

    public void setShoeName(String shoeName) {
        this.shoeName = shoeName;
    }
}
