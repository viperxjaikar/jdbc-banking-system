package com.banking.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection utility class.
 * Provides MySQL connection using JDBC.
 *
 * NOTE: Update the URL, USERNAME, and PASSWORD as per your MySQL setup.
 *
 * @author Gonuguntala Jaikar Ramu
 */
public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3307/banking_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        }
    }

    /**
     * Returns a new MySQL connection.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
