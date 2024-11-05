package com.example.project2_login.model;

public class MagicLinkRequest {
    private String email;

    // Constructor
    public MagicLinkRequest(String email) {
        this.email = email;
    }

    // Getter and Setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}