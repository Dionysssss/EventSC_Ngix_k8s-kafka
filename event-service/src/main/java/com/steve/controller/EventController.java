package com.steve.controller;

import com.steve.dto.ErrorResponse;
import com.steve.dto.IOEvent;
import com.steve.entity.Event;
import com.steve.service.CommentService;
import com.steve.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;
    @Autowired
    private CommentService commentService;

    // Endpoint to create a new event
    @PostMapping
    public ResponseEntity<ResponseMessage> createEvent(@RequestBody IOEvent ioEvent,
                                                       @RequestAttribute(value = "userId", required = false) Integer authUserId) {
        if (ioEvent.getEventCreatorId() == 0 && authUserId != null) {
            ioEvent.setEventCreatorId(authUserId);
        }
        if (ioEvent.getEventCreatorId() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("Missing eventCreatorId or Authorization token"));
        }
        IOEvent createdEvent = eventService.createEvent(ioEvent);
        return ResponseEntity.ok(new ResponseMessage("Event created successfully", createdEvent.getEventId()));
    }

    // GET /events with optional filters for category and date
    @GetMapping
    public ResponseEntity<List<IOEvent>> getAllEvents(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String date,
            @RequestParam(defaultValue = "no") String includeOutdated) {
        List<IOEvent> events;
        
        if (includeOutdated.compareToIgnoreCase("yes")==0){
            events = eventService.getAllEvents(category,date);
        }else{
            events = eventService.getUpcomingEvents(category);

        }

        return ResponseEntity.ok(events);
    }


    // GET /events/{id} to respond with IOEvent
    @GetMapping("/{id}")
    public ResponseEntity<IOEvent> getEventById(@PathVariable int id) {
        Event event = eventService.getEventById(id);

        // Check if event is found
        if (event == null) {
            return ResponseEntity.notFound().build(); // Respond with 404 if event not found
        }

        // Convert Event to IOEvent
        IOEvent ioEvent = eventService.convertToIOEvent(event);
        return ResponseEntity.ok(ioEvent);
    }

    // DELETE /events/{eventId} to delete an event by ID
    @DeleteMapping("/{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable int eventId,
                                         @RequestBody(required = false) Map<String, Integer> requestBody,
                                         @RequestAttribute(value = "userId", required = false) Integer authUserId) {
        Integer userId = authUserId != null ? authUserId : (requestBody != null ? requestBody.get("userId") : null);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "User ID is required."));
        }

        boolean isDeleted = eventService.deleteEvent(eventId, userId);
        if (isDeleted) {
            commentService.deleteCommentsByEventId(eventId);
            return ResponseEntity.ok(new ResponseMessage("Event deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(403, "You are not authorized to delete this event"));
        }
    }


    // Update class
    @PutMapping("/{eventId}")
    public ResponseEntity<?> editEvent(@PathVariable int eventId,
                                       @RequestBody IOEvent ioEvent,
                                       @RequestAttribute(value = "userId", required = false) Integer authUserId) {
        if (ioEvent.getEventCreatorId() == 0 && authUserId != null) {
            ioEvent.setEventCreatorId(authUserId);
        }
        if (ioEvent.getEventCreatorId() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(400, "Missing eventCreatorId or Authorization token"));
        }
        boolean isUpdated = eventService.editEvent(eventId, ioEvent);

        if (isUpdated) {
            return ResponseEntity.ok(new ResponseMessage("Event updated successfully", eventId));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse(403, "You are not authorized to edit this event"));
        }
    }


    // Inner class for response messages
    static class ResponseMessage {
        private String message;

        private int eventId;

        public ResponseMessage(String message, int eventId) {
            this.message = message;
            this.eventId = eventId;
        }

        public ResponseMessage(String message) {
            this.message = message;
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
