package com.example.project2_login.model;

/*
public class MagicLinkVerificationResponse {
    private String message;
    private boolean success;
    private String userId;  // or any other user-related data if needed

    public MagicLinkVerificationResponse(String message, boolean success, String userId) {
        this.message = message;
        this.success = success;
        this.userId = userId;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
*/
public class MagicLinkVerificationResponse {
    private String message; // Or any other fields returned by the backend

    // Getter and Setter
    public String getMessage() {return message;}

    public void setMessage(String message) {
        this.message = message;
    }
}