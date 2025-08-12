package com.soleexpressions.ecommercestore.DAOs;

import com.soleexpressions.ecommercestore.POJOs.Basket;
import com.soleexpressions.ecommercestore.POJOs.CustomizedShoe;

import java.util.List;

public interface BasketDAO {
    Basket getBasketByUserId(int userId) throws Exception;

    Basket createBasket(int userId) throws Exception;

    void addShoeToBasket(int customizedShoeId, int basketId) throws Exception;

    List<CustomizedShoe> getBasketContents(int basketId) throws Exception;

    void removeShoeFromBasket(int customizedShoeId) throws Exception;

    void clearBasket(int basketId) throws Exception;
}