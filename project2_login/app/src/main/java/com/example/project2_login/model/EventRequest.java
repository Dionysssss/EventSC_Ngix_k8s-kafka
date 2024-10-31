package com.example.project2_login.model;

public class EventRequest {
    private String name;
    private String location;
    private String time;
    private String description;
    private int creatorId;

    public EventRequest(String name, String location, String time, String description, int creatorId) {
        this.name = name;
        this.location = location;
        this.time = time;
        this.description = description;
        this.creatorId = creatorId;
    }
}
