package com.steve.dto;

public class RegistrationResponse {
    private String userId;
    private String message;

    public RegistrationResponse(String userId, String message) {
        this.userId = userId;
        this.message = message;
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
}
