package com.example.project2_login.model;

import java.util.List;

public class EventResponse {
    private int eventId;
    private String message;

    private String eventName;
    private Event.EventLocation eventLocation;
    private String eventDescription;
    private String eventDate;
    private String eventTime;
    private int eventCreatorId;
    private List<Comment> comments; // New field for comments

    private int confirmedCount;
    private int falseReportsCount;

    // Getters
    public int getEventId() {
        return eventId;
    }

    public String getMessage() {
        return message;
    }

    // Getter for confirmedCount
    public int getConfirmedCount() {
        return confirmedCount;
    }

    // (Optional) Getter for falseReportsCount if needed
    public int getFalseReportsCount() {
        return falseReportsCount;
    }

    public String getEventName() {
        return eventName;
    }

    public Event.EventLocation getEventLocation() {
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
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventLocation(Event.EventLocation eventLocation) {
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

    // Converts EventResponse to Event
    public Event toEvent() {
        Event event = new Event(
                String.valueOf(this.eventId),
                this.eventName,
                this.eventLocation,
                this.eventDescription,
                this.eventDate,
                this.eventTime,
                this.eventCreatorId
        );
        event.setComments(this.comments); // Set comments for the event
        return event;
    }
}