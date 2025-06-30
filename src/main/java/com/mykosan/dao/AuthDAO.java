package com.mykosan.dao;

import com.mykosan.utils.DBUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class AuthDAO {

    public boolean validateLogin(String username, String plainPassword) {
        String sql = "SELECT password FROM users WHERE username = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                return BCrypt.checkpw(plainPassword, hashedPassword); // ✅ hashing check
            }

        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
        }

        return false; // username tidak ditemukan atau error
    }
}
