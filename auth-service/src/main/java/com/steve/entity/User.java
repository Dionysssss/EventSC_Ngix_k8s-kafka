package com.steve.entity;

import org.apache.ibatis.jdbc.Null;

public class User {
    private int userId;
    private String email;
    private String password;
    private boolean isAuthenticated;

    // Default constructor
    public User() {}


    // Parameterized constructor
    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.isAuthenticated = false;
    }

    // Parameterized constructor
    public User(int userId, String email, String password, boolean isAuthenticated) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.isAuthenticated = isAuthenticated;
    }

    // Getters and setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isAuthenticated=" + isAuthenticated +
                '}';
    }
}
