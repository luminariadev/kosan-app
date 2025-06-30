package com.mykosan.model;

import java.time.LocalDateTime;

public class SharedPayment {
    private int paymentId;
    private int expenseId;
    private int userId;
    private int paidAmount;
    private LocalDateTime paidDate;

    public SharedPayment(int paymentId, int expenseId, int userId, int paidAmount, LocalDateTime paidDate) {
        this.paymentId = paymentId;
        this.expenseId = expenseId;
        this.userId = userId;
        this.paidAmount = paidAmount;
        this.paidDate = paidDate;
    }

    public int getPaymentId() { return paymentId; }
    public int getExpenseId() { return expenseId; }
    public int getUserId() { return userId; }
    public int getPaidAmount() { return paidAmount; }
    public LocalDateTime getPaidDate() { return paidDate; }

    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
    public void setExpenseId(int expenseId) { this.expenseId = expenseId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setPaidAmount(int paidAmount) { this.paidAmount = paidAmount; }
    public void setPaidDate(LocalDateTime paidDate) { this.paidDate = paidDate; }
}