package com.steve.entity;

import java.time.LocalDateTime;

public class Validation {
    private int validationId;
    private int eventId;
    private int userId;
    private boolean isConfirmed;
    private LocalDateTime createdAt;

    // Constructors
    public Validation() {}

    public Validation(int eventId, int userId, boolean isConfirmed) {
        this.eventId = eventId;
        this.userId = userId;
        this.isConfirmed = isConfirmed;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public int getValidationId() {
        return validationId;
    }

    public void setValidationId(int validationId) {
        this.validationId = validationId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Validation{" +
                "validationId=" + validationId +
                ", eventId=" + eventId +
                ", userId=" + userId +
                ", isConfirmed=" + isConfirmed +
                ", createdAt=" + createdAt +
                '}';
    }
}

