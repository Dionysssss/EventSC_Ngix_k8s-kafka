package com.example.project2_login.model;

public class CommentResponse {
    private int commentId;
    private String message;
    private String content;   // New field
    private String timestamp; // New field
    private int postedBy;     // New field
    private int eventId;      // New field

    public CommentResponse(int commentId, String message, String content, String timestamp, int postedBy, int eventId) {
        this.commentId = commentId;
        this.message = message;
        this.content = content;
        this.timestamp = timestamp;
        this.postedBy = postedBy;
        this.eventId = eventId;
    }

    // Getters and setters
    public int getCommentId() { return commentId; }
    public void setCommentId(int commentId) { this.commentId = commentId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public int getPostedBy() { return postedBy; }
    public void setPostedBy(int postedBy) { this.postedBy = postedBy; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }
}