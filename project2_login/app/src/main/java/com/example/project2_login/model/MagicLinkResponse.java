package com.example.project2_login.model;

public class MagicLinkResponse {
    private String message;

    public MagicLinkResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
