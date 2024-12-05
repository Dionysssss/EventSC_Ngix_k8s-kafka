package com.steve.entity;

import java.time.LocalDateTime;

public class VerificationToken {
    private int id;
    private String token;
    private int userId;
    private LocalDateTime expiration;

    // Constructor with all fields
    public VerificationToken(String token, int userId, LocalDateTime expiration) {
        this.token = token;
        this.userId = userId;
        this.expiration = expiration;
    }

    // Default constructor
    public VerificationToken() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public LocalDateTime getExpiration() { return expiration; }
    public void setExpiration(LocalDateTime expiration) { this.expiration = expiration; }
}