package com.example.project2_login.model;

import java.util.List;

public class EventRequest {
    private String eventName;
    private EventLocation eventLocation;
    private String eventDescription;
    private String eventDate;
    private String eventTime;
    private int eventCreatorId;
    private List<Comment> comments; // New field for comments

    public EventRequest(String eventName, double latitude, double longitude, String eventDescription, String eventDate, String eventTime, int eventCreatorId) {
        this.eventName = eventName;
        this.eventLocation = new EventLocation(latitude, longitude);
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventCreatorId = eventCreatorId;
    }

    // Getters
    public String getEventName() {
        return eventName;
    }

    public EventLocation getEventLocation() {
        return eventLocation;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public int getEventCreatorId() {
        return eventCreatorId;
    }

    public List<Comment> getComments() {
        return comments;
    }

    // Setters
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventLocation(EventLocation eventLocation) {
        this.eventLocation = eventLocation;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public void setEventCreatorId(int eventCreatorId) {
        this.eventCreatorId = eventCreatorId;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    // Nested class for location
    public static class EventLocation {
        private double latitude;
        private double longitude;

        public EventLocation(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        // Getters
        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        // Setters
        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }
}