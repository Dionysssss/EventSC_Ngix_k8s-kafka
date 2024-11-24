package com.steve.mapper;

import com.steve.entity.Comment;
import com.steve.entity.Event;
import com.steve.entity.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CommentMapperTest {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private UserMapper userMapper;

    static User user = null;
    static Event event = null;

    @BeforeEach
    void setUp() {
        // Clean up the database before each test
        // commentMapper.deleteCommentsByEventId(1);
        eventMapper.deleteAllEvents();
        userMapper.deleteAllUsers();

        // Insert a test user
        user = new User();
        user.setEmail("testuser@example.com");
        user.setPassword("password123");
        userMapper.insertUser(user);

        // Insert a test event
        event = new Event();
        event.setEventName("Test Event");
        event.setEventDescription("A test event description");
        event.setEventDate(LocalDate.of(2024, 11, 25));
        event.setEventTime(LocalTime.of(10, 0));
        event.setEventCreatorId(user.getUserId());
        eventMapper.insertEvent(event);
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        commentMapper.deleteCommentsByEventId(1);
        eventMapper.deleteAllEvents();
        userMapper.deleteAllUsers();
    }

    @Test
    void testInsertComment() {
        // Given
        Comment comment = new Comment();
        comment.setContent("This is a test comment.");
        comment.setTimestamp(LocalDateTime.now());
        comment.setPostedBy(user.getUserId());
        comment.setEventId(event.getEventId());

        // When comment Mapper.insertComment(comment);
        commentMapper.insertComment(comment);

        // Then
        assertNotNull(comment.getCommentId(), "Comment ID should be auto-generated");
        List<Comment> comments = commentMapper.findCommentsByEventId(event.getEventId());
        assertEquals(1, comments.size(), "There should be 1 comment for eventId=1");
        assertEquals("This is a test comment.", comments.get(0).getContent());
    }

    @Test
    void testFindCommentsByEventId() {
        // Given
        Comment comment1 = new Comment();
        comment1.setContent("Comment 1");
        comment1.setTimestamp(LocalDateTime.now());
        comment1.setPostedBy(user.getUserId()); // Test user ID
        comment1.setEventId(event.getEventId()); // Test event ID
        commentMapper.insertComment(comment1);

        Comment comment2 = new Comment();
        comment2.setContent("Comment 2");
        comment2.setTimestamp(LocalDateTime.now());
        comment2.setPostedBy(user.getUserId()); // Test user ID
        comment2.setEventId(event.getEventId()); // Test event ID
        commentMapper.insertComment(comment2);

        // When
        List<Comment> comments = commentMapper.findCommentsByEventId(event.getEventId());

        // Then
        assertEquals(2, comments.size(), "There should be 2 comments for eventId=1");
        assertEquals("Comment 1", comments.get(0).getContent());
        assertEquals("Comment 2", comments.get(1).getContent());
    }

    @Test
    void testDeleteCommentsByEventId() {
        // Given
        Comment comment = new Comment();
        comment.setContent("This is a test comment.");
        comment.setTimestamp(LocalDateTime.now());
        comment.setPostedBy(user.getUserId()); // Test user ID
        comment.setEventId(event.getEventId()); // Test event ID
        commentMapper.insertComment(comment);

        // When
        int rowsDeleted = commentMapper.deleteCommentsByEventId(event.getEventId());

        // Then
        assertEquals(1, rowsDeleted, "One comment should be deleted for eventId=1");
        List<Comment> comments = commentMapper.findCommentsByEventId(1);
        assertTrue(comments.isEmpty(), "No comments should exist for eventId=1");
    }
}
