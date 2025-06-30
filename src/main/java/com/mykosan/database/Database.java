package com.mykosan.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:src/main/resources/kosan.db";
    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DB_URL);
                System.out.println("Koneksi SQLite berhasil.");
            } catch (SQLException e) {
                System.out.println("Gagal koneksi DB: " + e.getMessage());
            }
        }
        return connection;
    }
}
