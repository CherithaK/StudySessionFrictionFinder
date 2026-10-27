package com.friction.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Opens MySQL connections. Settings can be changed with the environment
 * variables DB_URL, DB_USER and DB_PASSWORD.
 */
public class DBConnection {
    private static final String URL = env("DB_URL", "jdbc:mysql://localhost:3306/friction_finder");
    private static final String USER = env("DB_USER", "root");
    private static final String PASSWORD = env("DB_PASSWORD", "");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static String env(String key, String fallback) {
        String value = System.getenv(key);
        return value == null ? fallback : value;
    }
}
