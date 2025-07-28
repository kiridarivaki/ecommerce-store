package com.soleexpressions.ecommercestore.DAOs;

import com.soleexpressions.ecommercestore.POJOs.CustomizedShoe;

import java.sql.SQLException;
import java.util.List;

public interface CustomizedShoeDAO {
    CustomizedShoe getCustomizedShoeById(int id) throws SQLException;

    List<CustomizedShoe> getAllCustomizedShoes() throws SQLException;

    void addCustomizedShoe(CustomizedShoe shoe) throws SQLException;

    void updateCustomizedShoe(CustomizedShoe shoe) throws SQLException;

    void deleteCustomizedShoe(int id) throws SQLException;
}
