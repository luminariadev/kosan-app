package com.mykosan.dao;

import com.mykosan.model.SharedExpense;
import com.mykosan.utils.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SharedExpenseDAO {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<SharedExpense> getAllExpenses() {
        List<SharedExpense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM shared_expenses";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String dueDateStr = rs.getString("due_date");
                LocalDate dueDate = dueDateStr != null ? LocalDate.parse(dueDateStr, formatter) : null;
                expenses.add(new SharedExpense(
                        rs.getInt("expense_id"),
                        rs.getString("title"),
                        rs.getInt("amount"),
                        dueDate
                ));
            }
            System.out.println("✅ Berhasil memuat " + expenses.size() + " tagihan bersama");
        } catch (SQLException e) {
            System.err.println("❌ Gagal memuat tagihan bersama: " + e.getMessage());
            throw new RuntimeException("Gagal mengambil data tagihan: " + e.getMessage(), e);
        }
        return expenses;
    }

    public SharedExpense getExpenseById(int expenseId) {
        String sql = "SELECT * FROM shared_expenses WHERE expense_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, expenseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String dueDateStr = rs.getString("due_date");
                    LocalDate dueDate = dueDateStr != null ? LocalDate.parse(dueDateStr, formatter) : null;
                    return new SharedExpense(
                            rs.getInt("expense_id"),
                            rs.getString("title"),
                            rs.getInt("amount"),
                            dueDate
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Gagal memuat tagihan ID " + expenseId + ": " + e.getMessage());
            throw new RuntimeException("Gagal mengambil tagihan berdasarkan ID: " + e.getMessage(), e);
        }
        return null;
    }

    public boolean insertExpense(SharedExpense expense) {
        String sql = "INSERT INTO shared_expenses (title, amount, due_date) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, expense.getTitle());
            ps.setInt(2, expense.getAmount());
            ps.setString(3, expense.getDueDate() != null ? expense.getDueDate().format(formatter) : null);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Gagal menambahkan tagihan: " + e.getMessage());
            throw new RuntimeException("Gagal menambahkan tagihan: " + e.getMessage(), e);
        }
    }

    public boolean updateExpense(SharedExpense expense) {
        String sql = "UPDATE shared_expenses SET title = ?, amount = ?, due_date = ? WHERE expense_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, expense.getTitle());
            ps.setInt(2, expense.getAmount());
            ps.setString(3, expense.getDueDate() != null ? expense.getDueDate().format(formatter) : null);
            ps.setInt(4, expense.getExpenseId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Gagal memperbarui tagihan: " + e.getMessage());
            throw new RuntimeException("Gagal memperbarui tagihan: " + e.getMessage(), e);
        }
    }

    public boolean deleteExpense(int expenseId) {
        String sql = "DELETE FROM shared_expenses WHERE expense_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, expenseId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Gagal menghapus tagihan: " + e.getMessage());
            throw new RuntimeException("Gagal menghapus tagihan: " + e.getMessage(), e);
        }
    }
}