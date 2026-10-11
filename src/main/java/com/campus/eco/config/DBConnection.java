package com.campus.eco.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = System.getenv().getOrDefault(
            "CAMPUS_ECO_DB_URL",
            "jdbc:mysql://localhost:3306/campus_eco?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
    private static final String USER = System.getenv().getOrDefault("CAMPUS_ECO_DB_USER", "root");
    private static final String PASS = System.getenv().getOrDefault("CAMPUS_ECO_DB_PASSWORD", "");

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static boolean testConnection() {
        try (Connection c = getConnection()) {
            return c != null;
        } catch (SQLException e) {
            return false;
        }
    }
}
