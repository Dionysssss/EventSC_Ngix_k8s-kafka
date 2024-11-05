package com.example.project2_login.model;

import java.util.List;

public class Event {
    private String eventId;
    private String eventName;
    private EventLocation eventLocation;
    private String eventDescription;
    private String eventDate;
    private String eventTime;
    private int eventCreatorId;
    private List<Comment> comments; // New data member for comments

    // Nested class for event location
    public static class EventLocation {
        private double latitude;
        private double longitude;

        // Constructor
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

    // Constructor
    public Event(String eventId, String eventName, EventLocation eventLocation, String eventDescription, String eventDate, String eventTime, int eventCreatorId) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventCreatorId = eventCreatorId;
    }

    // Getters
    public String getEventId() {
        return eventId;
    }

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
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

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

    @Override
    public String toString() {
        return "Event{" +
                "eventId='" + eventId + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventLocation=" + eventLocation +
                ", eventDescription='" + eventDescription + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", eventTime='" + eventTime + '\'' +
                ", eventCreatorId=" + eventCreatorId +
                ", comments=" + comments +
                '}';
    }
}