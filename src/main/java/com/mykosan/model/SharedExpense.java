package com.mykosan.model;

import java.time.LocalDate;

public class SharedExpense {
    private int expenseId;
    private String title;
    private int amount;
    private LocalDate dueDate;

    public SharedExpense(int expenseId, String title, int amount, LocalDate dueDate) {
        this.expenseId = expenseId;
        this.title = title;
        this.amount = amount;
        this.dueDate = dueDate;
    }

    public int getExpenseId() { return expenseId; }
    public String getTitle() { return title != null ? title : ""; }
    public int getAmount() { return amount; }
    public LocalDate getDueDate() { return dueDate; }

    public void setExpenseId(int expenseId) { this.expenseId = expenseId; }
    public void setTitle(String title) { this.title = title; }
    public void setAmount(int amount) { this.amount = amount; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}