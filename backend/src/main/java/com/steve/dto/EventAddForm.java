package com.steve.dto;

import com.steve.entity.Event;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

public class EventAddForm {

    private String name;
    private String location;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime time;

    private String description;
    private int creatorId;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    // Method to convert EventAddForm to Event entity
    public Event toEvent() {
        Event event = new Event();
        event.setEventName(this.name);
        event.setEventLocation(this.location);
        event.setEventDescription(this.description);
        event.setEventCreatorId(this.creatorId);

        // Convert LocalDateTime to LocalDate and LocalTime for Event entity
        event.setEventDate(time.toLocalDate());
        event.setEventTime(time.toLocalTime());

        return event;
    }
}
