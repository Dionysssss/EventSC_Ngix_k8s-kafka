package com.example.project2_login.model;

public class MagicLinkVerificationRequest {
    private String email;
    private String verificationCode;

    public MagicLinkVerificationRequest(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
    }

    // Getters and setters if needed
}