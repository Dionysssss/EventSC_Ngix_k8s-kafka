package com.example.project2_login.model;

public class Event {
    private String event_id;
    private String name;
    private String location;
    private String time;
    private String description;
    private String creator;

    // Constructor
    public Event(String event_id, String name, String location, String time, String description, String creator) {
        this.event_id = event_id;
        this.name = name;
        this.location = location;
        this.time = time;
        this.description = description;
        this.creator = creator;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getCreator() {
        return creator;
    }
}