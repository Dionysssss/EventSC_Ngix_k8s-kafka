package com.steve.controller;


import com.steve.dto.ErrorResponse;
import com.steve.entity.Comment;
import com.steve.service.CommentService;
import com.steve.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private EventService eventService;

    // POST /comments to create a new comment
    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody Comment comment) {
        if (comment.getContent() == null || comment.getPostedBy() == 0 || comment.getEventId() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(400, "Invalid request format or missing fields"));
        }

        if(eventService.getEventById(comment.getEventId()) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, "Event Not Found"));

        }

        int commentId = commentService.createComment(comment);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseCommentId("Comment created successfully", commentId));
    }

    // GET /comments?eventId={eventId} to retrieve all comments for a specific event
    @GetMapping
    public ResponseEntity<?> getCommentsByEventId(@RequestParam int eventId) {
        List<Comment> comments = commentService.getCommentsByEventId(eventId);

        if (comments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, "No comments found for the specified event"));
        }

        return ResponseEntity.ok(comments);
    }

    // DELETE /comments/event/{eventId} to delete all comments for a specific event
    @DeleteMapping("/event/{eventId}")
    public ResponseEntity<?> deleteCommentsByEventId(@PathVariable int eventId) {

        //test if the event exist
        if(eventService.getEventById(eventId) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, "Event Not Found"));

        }

        int isDeleted = commentService.deleteCommentsByEventId(eventId);

        if (isDeleted > 0) {
            return ResponseEntity.ok(new ResponseMessage("All comments for the event deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, "No comments found for the specified event"));
        }
    }


    private class ResponseMessage{
        String message;

        public ResponseMessage(){}
        public ResponseMessage(String message) { this.message = message; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    private class ResponseCommentId {

        String message;
        Integer commentId;
        public ResponseCommentId(){ }
        public ResponseCommentId(String message, Integer commentId) { this.message = message; this.commentId = commentId; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Integer getCommentId() { return commentId; }
        public void setCommentId(Integer commentId) { this.commentId = commentId; }
    }
}
