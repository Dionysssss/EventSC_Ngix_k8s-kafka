package com.steve.dto;

public class LoginResponse {
    private String message;
    private String userId;
    private String token;


    public LoginResponse(String message) {
        this.message = message;
    }

    public LoginResponse(String userId, String message) {
        this.userId = userId;
        this.message = message;

    }

    public LoginResponse(String userId, String message, String token) {
        this.userId = userId;
        this.message = message;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
