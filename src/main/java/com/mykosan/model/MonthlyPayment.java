package com.mykosan.model;

import java.time.LocalDateTime;

public class MonthlyPayment {
    private int paymentId;
    private int userId;
    private int roomId;
    private String month;
    private int amount;
    private String status;
    private LocalDateTime paidDate;

    public MonthlyPayment(int paymentId, int userId, int roomId, String month, int amount, String status, LocalDateTime paidDate) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.roomId = roomId;
        this.month = month;
        this.amount = amount;
        this.status = status;
        this.paidDate = paidDate;
    }

    public int getPaymentId() { return paymentId; }
    public int getUserId() { return userId; }
    public int getRoomId() { return roomId; }
    public String getMonth() { return month != null ? month : ""; }
    public int getAmount() { return amount; }
    public String getStatus() { return status != null ? status : ""; }
    public LocalDateTime getPaidDate() { return paidDate; }

    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }
    public void setMonth(String month) { this.month = month; }
    public void setAmount(int amount) { this.amount = amount; }
    public void setStatus(String status) { this.status = status; }
    public void setPaidDate(LocalDateTime paidDate) { this.paidDate = paidDate; }
}