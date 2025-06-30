package com.mykosan.dao;

import com.mykosan.model.MonthlyPayment;
import com.mykosan.utils.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MonthlyPaymentDAO {
    private static final Logger logger = LoggerFactory.getLogger(MonthlyPaymentDAO.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<MonthlyPayment> getAllPayments() {
        List<MonthlyPayment> payments = new ArrayList<>();
        String sql = "SELECT * FROM monthly_payments";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LocalDateTime paidDate = null;
                String paidDateStr = rs.getString("paid_date");
                if (paidDateStr != null && !paidDateStr.isBlank()) {
                    try {
                        paidDate = LocalDateTime.parse(paidDateStr, formatter);
                    } catch (Exception e) {
                        logger.warn("Format tanggal tidak valid untuk paid_date='{}'", paidDateStr);
                    }
                }

                payments.add(new MonthlyPayment(
                        rs.getInt("payment_id"),
                        rs.getInt("user_id"),
                        rs.getInt("room_id"),
                        rs.getString("month"),
                        rs.getInt("amount"),
                        rs.getString("status"),
                        paidDate
                ));
            }

            logger.info("Berhasil memuat {} pembayaran", payments.size());

        } catch (SQLException e) {
            logger.error("Gagal mengambil data pembayaran: {}", e.getMessage(), e);
            throw new RuntimeException("Gagal mengambil data pembayaran: " + e.getMessage(), e);
        }

        return payments;
    }

    public MonthlyPayment getPaymentById(int paymentId) {
        String sql = "SELECT * FROM monthly_payments WHERE payment_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, paymentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String paidDateStr = rs.getString("paid_date");
                    LocalDateTime paidDate = null;
                    if (paidDateStr != null && !paidDateStr.isBlank()) {
                        try {
                            paidDate = LocalDateTime.parse(paidDateStr, formatter);
                        } catch (Exception e) {
                            logger.warn("Gagal parsing paid_date='{}'", paidDateStr);
                        }
                    }

                    return new MonthlyPayment(
                            rs.getInt("payment_id"),
                            rs.getInt("user_id"),
                            rs.getInt("room_id"),
                            rs.getString("month"),
                            rs.getInt("amount"),
                            rs.getString("status"),
                            paidDate
                    );
                }
            }

        } catch (SQLException e) {
            logger.error("Gagal mengambil pembayaran dengan ID {}: {}", paymentId, e.getMessage(), e);
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }

        return null;
    }


    public boolean insertPayment(MonthlyPayment payment) {
        String sql = "INSERT INTO monthly_payments (user_id, room_id, month, amount, status, paid_date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, payment.getUserId());
            ps.setInt(2, payment.getRoomId());
            ps.setString(3, payment.getMonth());
            ps.setInt(4, payment.getAmount());
            ps.setString(5, payment.getStatus());
            if (payment.getPaidDate() != null) {
                ps.setTimestamp(6, Timestamp.valueOf(payment.getPaidDate()));
            } else {
                ps.setNull(6, Types.TIMESTAMP);
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Gagal menambahkan pembayaran: {}", e.getMessage(), e);
            return false;
        }
    }

    public boolean updatePayment(MonthlyPayment payment) {
        String sql = "UPDATE monthly_payments SET user_id = ?, room_id = ?, month = ?, amount = ?, status = ?, paid_date = ? WHERE payment_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, payment.getUserId());
            ps.setInt(2, payment.getRoomId());
            ps.setString(3, payment.getMonth());
            ps.setInt(4, payment.getAmount());
            ps.setString(5, payment.getStatus());
            if (payment.getPaidDate() != null) {
                ps.setTimestamp(6, Timestamp.valueOf(payment.getPaidDate()));
            } else {
                ps.setNull(6, Types.TIMESTAMP);
            }
            ps.setInt(7, payment.getPaymentId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Gagal memperbarui pembayaran: {}", e.getMessage(), e);
            return false;
        }
    }

    public boolean deletePayment(int paymentId) {
        String sql = "DELETE FROM monthly_payments WHERE payment_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, paymentId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Gagal menghapus pembayaran: {}", e.getMessage(), e);
            return false;
        }
    }
}
