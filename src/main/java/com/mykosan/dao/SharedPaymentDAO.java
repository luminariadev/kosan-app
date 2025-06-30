package com.mykosan.dao;

import com.mykosan.model.SharedPayment;
import com.mykosan.utils.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SharedPaymentDAO {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<SharedPayment> getAllSharedPayments() {
        List<SharedPayment> payments = new ArrayList<>();
        String sql = "SELECT * FROM shared_payments";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String paidDateStr = rs.getString("paid_date");
                LocalDateTime paidDate = paidDateStr != null ? LocalDateTime.parse(paidDateStr, formatter) : null;
                payments.add(new SharedPayment(
                        rs.getInt("payment_id"),
                        rs.getInt("expense_id"),
                        rs.getInt("user_id"),
                        rs.getInt("paid_amount"),
                        paidDate
                ));
            }
            System.out.println("✅ Berhasil memuat " + payments.size() + " pembayaran bersama");
        } catch (SQLException e) {
            System.err.println("❌ Gagal memuat pembayaran bersama: " + e.getMessage());
            throw new RuntimeException("Gagal mengambil data pembayaran bersama: " + e.getMessage(), e);
        }
        return payments;
    }

    public SharedPayment getSharedPaymentById(int paymentId) {
        String sql = "SELECT * FROM shared_payments WHERE payment_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String paidDateStr = rs.getString("paid_date");
                    LocalDateTime paidDate = paidDateStr != null ? LocalDateTime.parse(paidDateStr, formatter) : null;
                    return new SharedPayment(
                            rs.getInt("payment_id"),
                            rs.getInt("expense_id"),
                            rs.getInt("user_id"),
                            rs.getInt("paid_amount"),
                            paidDate
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Gagal memuat pembayaran bersama ID " + paymentId + ": " + e.getMessage());
            throw new RuntimeException("Gagal mengambil pembayaran bersama berdasarkan ID: " + e.getMessage(), e);
        }
        return null;
    }

    public boolean insertSharedPayment(SharedPayment payment) {
        String sql = "INSERT INTO shared_payments (expense_id, user_id, paid_amount, paid_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, payment.getExpenseId());
            ps.setInt(2, payment.getUserId());
            ps.setInt(3, payment.getPaidAmount());
            ps.setString(4, payment.getPaidDate() != null ? payment.getPaidDate().format(formatter) : null);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Gagal menambahkan pembayaran bersama: " + e.getMessage());
            throw new RuntimeException("Gagal menambahkan pembayaran bersama: " + e.getMessage(), e);
        }
    }

    public boolean updateSharedPayment(SharedPayment payment) {
        String sql = "UPDATE shared_payments SET expense_id = ?, user_id = ?, paid_amount = ?, paid_date = ? WHERE payment_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, payment.getExpenseId());
            ps.setInt(2, payment.getUserId());
            ps.setInt(3, payment.getPaidAmount());
            ps.setString(4, payment.getPaidDate() != null ? payment.getPaidDate().format(formatter) : null);
            ps.setInt(5, payment.getPaymentId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Gagal memperbarui pembayaran bersama: " + e.getMessage());
            throw new RuntimeException("Gagal memperbarui pembayaran bersama: " + e.getMessage(), e);
        }
    }

    public boolean deleteSharedPayment(int paymentId) {
        String sql = "DELETE FROM shared_payments WHERE payment_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Gagal menghapus pembayaran bersama: " + e.getMessage());
            throw new RuntimeException("Gagal menghapus pembayaran bersama: " + e.getMessage(), e);
        }
    }
}