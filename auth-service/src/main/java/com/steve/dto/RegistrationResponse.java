package com.steve.dto;

public class RegistrationResponse {
    private String userId;
    private String message;
    private String token;

    public RegistrationResponse(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public RegistrationResponse(String userId, String message, String token) {
        this.userId = userId;
        this.message = message;
        this.token = token;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
