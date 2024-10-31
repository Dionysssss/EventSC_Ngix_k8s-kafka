package com.example.project2_login.model;

public class MagicLinkRequest {
    private String email;

    public MagicLinkRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
