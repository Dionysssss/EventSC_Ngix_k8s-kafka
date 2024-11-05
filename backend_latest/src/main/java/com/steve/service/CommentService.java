package com.steve.service;

import com.steve.entity.Comment;
import com.steve.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    public int createComment(Comment comment) {
        commentMapper.insertComment(comment);
        return comment.getCommentId(); // Returns the generated commentId after insertion
    }

    // Get comments
    public List<Comment> getCommentsByEventId(int eventId) {
        return commentMapper.findCommentsByEventId(eventId);
    }

    // Delete comments
    // Return the number of comments delete
    public int deleteCommentsByEventId(int eventId) {
        int rowsAffected = commentMapper.deleteCommentsByEventId(eventId);
        return rowsAffected; // Returns true if any comments were deleted
    }
}

