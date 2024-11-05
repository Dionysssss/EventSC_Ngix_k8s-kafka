package com.example.project2_login.model;

public class UserResponse {
    private int userId;
    private String email;

    // Getters
    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    // Setters
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

