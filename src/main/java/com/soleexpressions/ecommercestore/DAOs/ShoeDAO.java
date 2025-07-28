package com.soleexpressions.ecommercestore.DAOs;

import com.soleexpressions.ecommercestore.POJOs.Shoe;

import java.util.List;

public interface ShoeDAO {
    List<Shoe> getAllShoes();

    Shoe getShoeById(int id);
}