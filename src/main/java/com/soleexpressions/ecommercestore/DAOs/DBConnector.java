package com.soleexpressions.ecommercestore.DAOs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    private final String dbServer = "localhost";
    private final String dbServerPort = "5433";
    private final String dbName = "soleexpressions";
    private final String dbusername = "postgres";
    private final String dbpassword = "1234";

    private Connection con = null;

    public Connection getConnection() throws Exception {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new Exception("PostgreSQL Driver error: " + e.getMessage(), e);
        }

        try {
            con = DriverManager.getConnection("jdbc:postgresql://"
                    + dbServer + ":" + dbServerPort + "/" + dbName, dbusername, dbpassword);
            return con;
        } catch (SQLException e) {
            throw new Exception("Could not establish connection with the Database Server: "
                    + e.getMessage(), e);
        }
    }

    public void close() throws SQLException {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            throw new SQLException("Could not close connection with the Database Server: "
                    + e.getMessage(), e);
        }
    }
}