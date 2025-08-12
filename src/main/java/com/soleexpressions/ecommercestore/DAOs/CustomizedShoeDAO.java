package com.soleexpressions.ecommercestore.DAOs;

import com.soleexpressions.ecommercestore.POJOs.CustomizedShoe;

import java.util.List;

public interface CustomizedShoeDAO {
    CustomizedShoe getCustomizedShoeById(int id) throws Exception;

    List<CustomizedShoe> getAllCustomizedShoes() throws Exception;

    void addCustomizedShoe(CustomizedShoe shoe) throws Exception;

    void updateCustomizedShoe(CustomizedShoe shoe) throws Exception;

    void deleteCustomizedShoe(int id) throws Exception;
}
