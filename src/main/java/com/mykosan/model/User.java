package com.mykosan.model;

public class User {
    private int userId;
    private String fullName;
    private String username;
    private String email;
    private String contact;
    private String role;
    private String password;

    public User(int userId, String fullName, String username, String email, String contact, String role, String password) {
        this.userId = userId;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.contact = contact;
        this.role = role;
        this.password = password;
    }

    public int getUserId() { return userId; }
    public String getFullName() { return fullName != null ? fullName : ""; }
    public String getUsername() { return username != null ? username : ""; }
    public String getEmail() { return email != null ? email : ""; }
    public String getContact() { return contact != null ? contact : ""; }
    public String getRole() { return role != null ? role : ""; }
    public String getPassword() { return password != null ? password : ""; }

    public void setUserId(int userId) { this.userId = userId; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setContact(String contact) { this.contact = contact; }
    public void setRole(String role) { this.role = role; }
    public void setPassword(String password) { this.password = password; }
}