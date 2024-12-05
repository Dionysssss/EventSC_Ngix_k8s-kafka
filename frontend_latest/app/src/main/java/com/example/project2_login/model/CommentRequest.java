package com.example.project2_login.model;

public class CommentRequest {
    private String content;
    private int postedBy;
    private int eventId;

    public CommentRequest(String content, int postedBy, int eventId) {
        this.content = content;
        this.postedBy = postedBy;
        this.eventId = eventId;
    }

    // Getters and setters
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public int getPostedBy() { return postedBy; }
    public void setPostedBy(int postedBy) { this.postedBy = postedBy; }
    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }
}