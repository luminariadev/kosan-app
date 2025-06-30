package com.mykosan.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String DB_URL = "jdbc:sqlite:D:/Kuliah/Semester 4/Praktikum PBO & PBO/Aplikasi Manajemen Keuangan Kosan Berbasis Kolektif/database/kosan.db";
    private static Connection connection = null;

    // Method untuk mendapatkan koneksi database
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(DB_URL);
                System.out.println("✅ Koneksi ke database berhasil.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver SQLite tidak ditemukan: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("❌ Gagal koneksi ke database: " + e.getMessage());
        }

        return connection;
    }


    // Method untuk menutup koneksi database
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("🔌 Koneksi database ditutup.");
            } catch (SQLException e) {
                System.err.println("❌ Gagal menutup koneksi: " + e.getMessage());
            }
        }
    }
}
