package com.steve.controller;


import com.steve.dto.ErrorResponse;
import com.steve.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.util.Map;

@RestController
@RequestMapping("/events")
public class ValidationController {

    @Autowired
    private ValidationService validationService;

    // Confirm an event
    @PostMapping("/{eventId}/confirm")
    public ResponseEntity<?> confirmEvent(@PathVariable int eventId,
                                          @RequestBody(required = false) Map<String, Object> userIdPayload,
                                          @RequestAttribute(value = "userId", required = false) Integer authUserId) {
        Integer userId = authUserId != null ? authUserId : (userIdPayload != null ? (Integer) userIdPayload.get("userId") : null);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "User ID is required."));
        }
        try {
            System.out.println("eventId: "+ eventId + " userId: " + userId);
            int confirmedCount = validationService.confirmEvent(eventId, userId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseMessage("Event confirmed successfully", eventId, confirmedCount));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(409, e.getMessage()));
        }
    }

    // Withdraw confirmation
    @DeleteMapping("/{eventId}/confirm")
    public ResponseEntity<?> withdrawConfirmation(@PathVariable int eventId,
                                                  @RequestBody(required = false) Map<String, Object> userIdPayload,
                                                  @RequestAttribute(value = "userId", required = false) Integer authUserId) {
        Integer userId = authUserId != null ? authUserId : (userIdPayload != null ? (Integer) userIdPayload.get("userId") : null);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "User ID is required."));
        }
        try {
            int confirmedCount = validationService.withdrawConfirmation(eventId, userId);
            return ResponseEntity.ok(new ResponseMessage("Confirmation withdrawn successfully", eventId, confirmedCount));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, e.getMessage()));
        }
    }

    // Report an event as false
    @PostMapping("/{eventId}/report-false")
    public ResponseEntity<?> reportEventAsFalse(@PathVariable int eventId,
                                                @RequestBody(required = false) Map<String, Object> userIdPayload,
                                                @RequestAttribute(value = "userId", required = false) Integer authUserId) {
        Integer userId = authUserId != null ? authUserId : (userIdPayload != null ? (Integer) userIdPayload.get("userId") : null);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "User ID is required."));
        }
        try {
            int falseReportsCount = validationService.reportEventAsFalse(eventId, userId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseMessage("Event reported as false successfully", eventId, falseReportsCount));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(409, e.getMessage()));
        }
    }

    // Withdraw false report
    @DeleteMapping("/{eventId}/report-false")
    public ResponseEntity<?> withdrawFalseReport(@PathVariable int eventId,
                                                 @RequestBody(required = false) Map<String, Object> userIdPayload,
                                                 @RequestAttribute(value = "userId", required = false) Integer authUserId) {
        Integer userId = authUserId != null ? authUserId : (userIdPayload != null ? (Integer) userIdPayload.get("userId") : null);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "User ID is required."));
        }
        try {
            int falseReportsCount = validationService.withdrawFalseReport(eventId, userId);
            return ResponseEntity.ok(new ResponseMessage("False report withdrawn successfully", eventId, falseReportsCount));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, e.getMessage()));
        }
    }

    // Retrieve validation counts
    @GetMapping("/{eventId}/validation-counts")
    public ResponseEntity<?> getValidationCounts(@PathVariable int eventId) {
        try {
            Map validationCounts = validationService.getValidationCounts(eventId);
            return ResponseEntity.ok(validationCounts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "Event not found"));
        }
    }

    // Get validation status for an event by user ID
    @GetMapping("/{eventId}/validation-status/{userId}")
    public ResponseEntity<?> getValidationStatus(@PathVariable int eventId,
                                                 @PathVariable int userId,
                                                 @RequestAttribute(value = "userId", required = false) Integer authUserId) {
        if (userId == 0 && authUserId != null) {
            userId = authUserId;
        }
        if (userId == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "User ID is required."));
        }
        try {
            String validationStatus = validationService.getValidationStatus(eventId, userId);
            System.out.println("validationStatus: "+validationStatus);
            return ResponseEntity.ok(new ValidationStatusResponse(validationStatus));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "Event or User not found"));
        }
    }



    public class ResponseMessage {
        private String message;
        private int eventId;
        private int count;

        // Constructors
        public ResponseMessage(String message) { this.message = message; }
        public ResponseMessage(String message, int eventId, int count) { this.message = message; this.eventId = eventId; this.count = count; }

        // Getters and Setters
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public int getEventId() { return eventId; }
        public void setEventId(int eventId) { this.eventId = eventId; }
        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }

        @Override
        public String toString() { return "ResponseMessage{message='" + message + "', eventId=" + eventId + ", count=" + count + '}'; }
    }

    public class ValidationStatusResponse {
        private String validationStatus;

        public ValidationStatusResponse(String validationStatus) { this.validationStatus = validationStatus; }

        public String getValidationStatus() { return validationStatus; }
        public void setValidationStatus(String validationStatus) { this.validationStatus = validationStatus; }

        @Override
        public String toString() { return "ValidationStatusResponse{validationStatus='" + validationStatus + "'}"; }
    }

}
