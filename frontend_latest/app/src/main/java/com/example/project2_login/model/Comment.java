package com.example.project2_login.model;

public class Comment {
    private int commentId;
    private String content;
    private String timestamp; // assuming a timestamp is also part of the comment
    private int postedBy;
    private int eventId;

    // Constructor with the specified parameters
    public Comment(int commentId, String content, String timestamp, int postedBy, int eventId) {
        this.commentId = commentId;
        this.content = content;
        this.timestamp = timestamp;
        this.postedBy = postedBy;
        this.eventId = eventId;
    }

    // Overloaded constructor without timestamp
    public Comment(int commentId, String content, int postedBy, int eventId) {
        this.commentId = commentId;
        this.content = content;
        this.postedBy = postedBy;
        this.eventId = eventId;
        this.timestamp = null; // Or set it to the current time if needed
    }

    // Getters and Setters
    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(int postedBy) {
        this.postedBy = postedBy;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
}