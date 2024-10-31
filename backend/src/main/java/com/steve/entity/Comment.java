package com.steve.entity;

import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

public class Comment {
    private int commentId;
    private String content;
    private Timestamp timestamp;
    private int postedBy;
    private int eventId;

    // Default constructor
    public Comment() {}

    // Parameterized constructor
    public Comment(int commentId, String content, Timestamp timestamp, int postedBy, int eventId) {
        this.commentId = commentId;
        this.content = content;
        this.timestamp = timestamp;
        this.postedBy = postedBy;
        this.eventId = eventId;
    }

    // Getters and setters
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
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
