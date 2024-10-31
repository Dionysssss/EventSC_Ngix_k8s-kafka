package com.steve.controller;

import com.steve.dto.EventAddForm;
import com.steve.entity.Event;
import com.steve.entity.User;
import com.steve.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    // Endpoint to create a new event
    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody EventAddForm eventAddForm) {
        Event event = eventAddForm.toEvent();

        Event createdEvent = eventService.createEvent(event);

        return ResponseEntity.ok(new ResponseMessage("Event created successfully", createdEvent.getEventId()));
    }

    // Endpoint to retrieve all events with optional filters
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String date) {
        List<Event> events = eventService.getAllEvents(category, date);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable int id) {
        Event event = eventService.getUserById(id);
        return ResponseEntity.ok(event);
    }

    // Inner class for response messages
    static class ResponseMessage {
        private String message;
        private int eventId;

        public ResponseMessage(String message, int eventId) {
            this.message = message;
            this.eventId = eventId;
        }

        // Getters and setters
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getEventId() {
            return eventId;
        }

        public void setEventId(int eventId) {
            this.eventId = eventId;
        }
    }
}
