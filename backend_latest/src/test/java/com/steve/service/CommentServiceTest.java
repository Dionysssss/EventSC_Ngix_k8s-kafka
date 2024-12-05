package com.steve.service;

import com.steve.entity.Comment;
import com.steve.entity.Event;
import com.steve.entity.User;
import com.steve.mapper.CommentMapper;
import com.steve.mapper.EventMapper;
import com.steve.mapper.UserMapper;
import com.steve.utils.TestLogger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(TestLogger.class)
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private UserMapper userMapper;

    // Global static User and Event instances
    private static User testUser;
    private static Event testEvent;

    @BeforeAll
    void setUp() {
        // Clean up the database
        commentMapper.deleteCommentsByEventId(1); // Assuming test Event ID will be 1
        eventMapper.deleteAllEvents();
        userMapper.deleteAllUsers();

        // Insert a test User
        testUser = new User();
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("password123");
        userMapper.insertUser(testUser);

        // Insert a test Event
        testEvent = new Event();
        testEvent.setEventName("Test Event");
        testEvent.setEventDescription("This is a test event.");
        testEvent.setEventDate(LocalDate.now());
        testEvent.setEventTime(LocalTime.now());
        testEvent.setEventCreatorId(testUser.getUserId());
        eventMapper.insertEvent(testEvent);
    }

    @AfterAll
    void tearDown() {
        // Clean up the database
        commentMapper.deleteCommentsByEventId(testEvent.getEventId());
        eventMapper.deleteAllEvents();
        userMapper.deleteAllUsers();
    }

    @AfterEach
    void cleanUpComment(){
        commentMapper.deleteCommentsByEventId(testEvent.getEventId());
    }

    @Test
    void testCreateComment() {
        // Given
        Comment comment = new Comment();
        comment.setContent("This is a test comment.");
        comment.setTimestamp(LocalDateTime.now());
        comment.setPostedBy(testUser.getUserId());
        comment.setEventId(testEvent.getEventId());

        // When
        int commentId = commentService.createComment(comment);

        // Then
        assertNotEquals(0, commentId, "Comment ID should be auto-generated");
        Comment fetchedComment = commentMapper.findCommentsByEventId(testEvent.getEventId()).get(0);
        assertNotNull(fetchedComment, "Comment should exist in the database");
        assertEquals("This is a test comment.", fetchedComment.getContent());
    }

    @Test
    void testGetCommentsByEventId() {
        // Given
        Comment comment1 = new Comment();
        comment1.setContent("First comment");
        comment1.setTimestamp(LocalDateTime.now());
        comment1.setPostedBy(testUser.getUserId());
        comment1.setEventId(testEvent.getEventId());
        commentService.createComment(comment1);

        Comment comment2 = new Comment();
        comment2.setContent("Second comment");
        comment2.setTimestamp(LocalDateTime.now());
        comment2.setPostedBy(testUser.getUserId());
        comment2.setEventId(testEvent.getEventId());
        commentService.createComment(comment2);

        // When
        List<Comment> comments = commentService.getCommentsByEventId(testEvent.getEventId());

        // Then
        assertEquals(2, comments.size(), "There should be 2 comments for the event");
        assertTrue(comments.stream().anyMatch(c -> c.getContent().equals("First comment")), "First comment should exist");
        assertTrue(comments.stream().anyMatch(c -> c.getContent().equals("Second comment")), "Second comment should exist");
    }

    @Test
    void testDeleteCommentsByEventId() {
        // Given
        Comment comment = new Comment();
        comment.setContent("Comment to be deleted");
        comment.setTimestamp(LocalDateTime.now());
        comment.setPostedBy(testUser.getUserId());
        comment.setEventId(testEvent.getEventId());
        commentService.createComment(comment);

        // When
        int rowsDeleted = commentService.deleteCommentsByEventId(testEvent.getEventId());

        // Then
        assertEquals(1, rowsDeleted, "One comment should be deleted");
        List<Comment> comments = commentService.getCommentsByEventId(testEvent.getEventId());
        assertTrue(comments.isEmpty(), "No comments should exist for the event after deletion");
    }
}
