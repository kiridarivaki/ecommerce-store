package com.soleexpressions.ecommercestore.DAOs;

import com.soleexpressions.ecommercestore.POJOs.Shoe;

import java.util.List;

public interface ShoeDAO {
    List<Shoe> getAllShoes() throws Exception;

    Shoe getShoeById(int id) throws Exception;
}