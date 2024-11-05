package com.steve.dto;

public class LoginResponse {
    private String message;
    private String userId;


    public LoginResponse(String message) {
        this.message = message;
    }

    public LoginResponse(String userId, String message) {
        this.userId = userId;
        this.message = message;

    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
