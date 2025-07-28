package com.soleexpressions.ecommercestore.POJOs;

import java.util.List;


public class Shoe {
    private int id;
    private String model;
    private double baseCost;
    private List<String> availableSizes;

    public Shoe() {
    }

    public Shoe(String model, double baseCost, List<String> availableSizes) {
        this.model = model;
        this.baseCost = baseCost;
        this.availableSizes = availableSizes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getBaseCost() {
        return baseCost;
    }

    public void setBaseCost(double baseCost) {
        this.baseCost = baseCost;
    }

    public List<String> getAvailableSizes() {
        return availableSizes;
    }

    public void setAvailableSizes(List<String> availableSizes) {
        this.availableSizes = availableSizes;
    }
}

