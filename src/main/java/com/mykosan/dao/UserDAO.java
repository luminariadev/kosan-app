package com.mykosan.dao;

import com.mykosan.model.User;
import com.mykosan.utils.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            logger.info("Berhasil mengambil {} pengguna", users.size());
        } catch (SQLException e) {
            logger.error("Gagal mengambil data pengguna: {}", e.getMessage(), e);
            throw new RuntimeException("Gagal mengambil data pengguna", e);
        }
        return users;
    }

    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = mapResultSetToUser(rs);
                logger.info("Berhasil mengambil pengguna dengan ID: {}", userId);
                return user;
            }
            logger.warn("Pengguna dengan ID: {} tidak ditemukan", userId);
        } catch (SQLException e) {
            logger.error("Gagal mengambil pengguna berdasarkan ID: {}", e.getMessage(), e);
            throw new RuntimeException("Gagal mengambil pengguna berdasarkan ID", e);
        }
        return null;
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE LOWER(username) = LOWER(?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = mapResultSetToUser(rs);
                logger.info("Berhasil mengambil pengguna dengan username: {}", username);
                return user;
            }
            logger.warn("Pengguna dengan username: {} tidak ditemukan", username);
        } catch (SQLException e) {
            logger.error("Gagal mengambil pengguna berdasarkan username: {}", e.getMessage(), e);
            throw new RuntimeException("Gagal mengambil pengguna berdasarkan username", e);
        }
        return null;
    }

    public boolean insertUser(User user) {
        String sql = "INSERT INTO users (full_name, username, email, contact, role, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getContact());
            ps.setString(5, user.getRole());
            ps.setString(6, user.getPassword());
            int rowsAffected = ps.executeUpdate();
            logger.info("Berhasil menambahkan pengguna: {}, rows affected: {}", user.getUsername(), rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Gagal menambahkan pengguna: {}", e.getMessage(), e);
            throw new RuntimeException("Gagal menambahkan pengguna", e);
        }
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE users SET full_name = ?, username = ?, email = ?, contact = ?, role = ?, password = ? WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getContact());
            ps.setString(5, user.getRole());
            ps.setString(6, user.getPassword());
            ps.setInt(7, user.getUserId());
            int rowsAffected = ps.executeUpdate();
            logger.info("Berhasil memperbarui pengguna ID: {}, rows affected: {}", user.getUserId(), rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Gagal memperbarui pengguna: {}", e.getMessage(), e);
            throw new RuntimeException("Gagal memperbarui pengguna", e);
        }
    }

    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            int rowsAffected = ps.executeUpdate();
            logger.info("Berhasil menghapus pengguna ID: {}, rows affected: {}", userId, rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Gagal menghapus pengguna: {}", e.getMessage(), e);
            throw new RuntimeException("Gagal menghapus pengguna", e);
        }
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("full_name"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("contact"),
                rs.getString("role"),
                rs.getString("password")
        );
    }
}