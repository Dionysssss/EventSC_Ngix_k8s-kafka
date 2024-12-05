package com.steve.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.steve.dto.IOEvent;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class Event {
    private int event_id;
    private String eventName;
    private String eventDescription;
    private LocalDate eventDate;
    private LocalTime eventTime;
    private int eventCreatorId;
    private String eventLocation; // Stored as JSON in the database

    // Getters and Setters
    public int getEventId() {
        return event_id;
    }

    public void setEventId(int eventId) {
        this.event_id = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public LocalTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalTime eventTime) {
        this.eventTime = eventTime;
    }

    public int getEventCreatorId() {
        return eventCreatorId;
    }

    public void setEventCreatorId(int eventCreatorId) {
        this.eventCreatorId = eventCreatorId;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    // Helper method to serialize an EventLocation object to JSON
    public void setEventLocationFromObject(IOEvent.EventLocation location) {
        try {
            this.eventLocation = new ObjectMapper().writeValueAsString(location);
        } catch (IOException e) {
            e.printStackTrace();
            this.eventLocation = null;
        }
    }

    // Helper method to deserialize JSON to an EventLocation object
    public IOEvent.EventLocation getEventLocationAsObject() {
        try {
            return new ObjectMapper().readValue(this.eventLocation, IOEvent.EventLocation.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
