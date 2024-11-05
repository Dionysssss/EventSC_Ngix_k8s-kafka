package com.example.project2_login.model;
import com.example.project2_login.model.Event;

public class ReceivedEvent {
    private Integer eventId;
    private String eventName;
    private EventLocation eventLocation;
    private String eventDate;
    private String eventTime;
    private String eventDescription;
    private Integer eventCreatorId;

    public ReceivedEvent(){
    }

    // Nested class for event location
    public static class EventLocation {
        private double latitude;
        private double longitude;

        @Override
        public String toString() {
            return "EventLocation{" +
                    "latitude=" + latitude +
                    ", longitude=" + longitude +
                    '}';
        }

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

    public ReceivedEvent(Integer eventId, String eventName, EventLocation eventLocation, String eventDescription, Integer eventCreatorId) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventDescription = eventDescription;
        this.eventCreatorId = eventCreatorId;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public EventLocation getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(EventLocation eventLocation) {
        this.eventLocation = eventLocation;
    }


    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public Integer getEventCreatorId() {
        return eventCreatorId;
    }

    public void setEventCreatorId(Integer eventCreatorId) {
        this.eventCreatorId = eventCreatorId;
    }

    public Event toEvent() {
        return new Event(
                String.valueOf(this.eventId), // Convert Integer to String
                this.eventName,
                new Event.EventLocation(this.eventLocation.getLatitude(), this.eventLocation.getLongitude()),
                this.eventDescription,
                this.eventDate,
                this.eventTime,
                this.eventCreatorId
        );
    }
}