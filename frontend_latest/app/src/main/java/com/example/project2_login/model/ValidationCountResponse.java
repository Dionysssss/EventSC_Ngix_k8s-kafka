package com.example.project2_login.model;

public class ValidationCountResponse {
    private int eventId;
    private int confirmedCount;
    private int falseReportsCount;

    public int getEventId() {
        return eventId;
    }

    public int getConfirmedCount() {
        return confirmedCount;
    }

    public int getFalseReportsCount() {
        return falseReportsCount;
    }
}
