package com.steve.mapper;

import com.steve.entity.Comment;
import com.steve.entity.Event;
import com.steve.entity.User;
import com.steve.entity.Validation;
import com.steve.utils.TestLogger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(TestLogger.class)
public class ValidationMapperTest {

    @Autowired
    private ValidationMapper validationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private CommentMapper commentMapper;

    // Global static objects
    private static User testUser;
    private static Event testEvent;
    private static Comment testComment;

    @BeforeAll
    void setUp() {
        // Clean up database
        validationMapper.deleteValidation(1, 1, true);
        validationMapper.deleteValidation(1, 1, false);
        commentMapper.deleteCommentsByEventId(1);
        eventMapper.deleteAllEvents();
        userMapper.deleteAllUsers();

        // Insert User
        testUser = new User();
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("password123");
        userMapper.insertUser(testUser);

        // Insert Event
        testEvent = new Event();
        testEvent.setEventName("Test Event");
        testEvent.setEventDescription("A test event description");
        testEvent.setEventDate(java.time.LocalDate.of(2024, 11, 25));
        testEvent.setEventTime(java.time.LocalTime.of(10, 0));
        testEvent.setEventCreatorId(testUser.getUserId());
        eventMapper.insertEvent(testEvent);

        // Insert Comment
        testComment = new Comment();
        testComment.setContent("Test Comment");
        testComment.setTimestamp(java.time.LocalDateTime.now());
        testComment.setPostedBy(testUser.getUserId());
        testComment.setEventId(testEvent.getEventId());
        commentMapper.insertComment(testComment);
    }

    @AfterAll
    void tearDown() {
        // Clean up database
        validationMapper.deleteValidation(testEvent.getEventId(), testUser.getUserId(), true);
        validationMapper.deleteValidation(testEvent.getEventId(), testUser.getUserId(), false);
        commentMapper.deleteCommentsByEventId(testEvent.getEventId());
        eventMapper.deleteAllEvents();
        userMapper.deleteAllUsers();
    }

    @AfterEach
    void cleanValidation(){
        validationMapper.deleteValidation(testEvent.getEventId(), testUser.getUserId(), true);
        validationMapper.deleteValidation(testEvent.getEventId(), testUser.getUserId(), false);
    }

    @Test
    void testInsertValidation() {
        // When
        validationMapper.insertValidation(testEvent.getEventId(), testUser.getUserId(), true);

        // Then
        Validation validation = validationMapper.findValidation(testEvent.getEventId(), testUser.getUserId());
        assertNotNull(validation, "Validation should be inserted");
        assertEquals(testEvent.getEventId(), validation.getEventId());
        assertEquals(testUser.getUserId(), validation.getUserId());

    }

    @Test
    void testDeleteValidation() {
        // Insert validation first
        validationMapper.insertValidation(testEvent.getEventId(), testUser.getUserId(), true);

        // When
        int rowsDeleted = validationMapper.deleteValidation(testEvent.getEventId(), testUser.getUserId(), true);

        // Then
        assertEquals(1, rowsDeleted, "One validation should be deleted");
        Validation validation = validationMapper.findValidation(testEvent.getEventId(), testUser.getUserId());
        assertNull(validation, "Validation should be deleted");
    }

    @Test
    void testCountConfirmed() {
        // Insert confirmed validations
        validationMapper.insertValidation(testEvent.getEventId(), testUser.getUserId(), true);

        // When
        int confirmedCount = validationMapper.countConfirmed(testEvent.getEventId());

        // Then
        assertEquals(1, confirmedCount, "There should be 1 confirmed validation");
    }

    @Test
    void testCountFalseReports() {
        // Insert false report
        validationMapper.insertValidation(testEvent.getEventId(), testUser.getUserId(), false);

        // When
        int falseReportsCount = validationMapper.countFalseReports(testEvent.getEventId());

        // Then
        assertEquals(1, falseReportsCount, "There should be 1 false report");
    }

    @Test
    void testGetValidationStatus() {
        // Insert confirmed validation
        validationMapper.insertValidation(testEvent.getEventId(), testUser.getUserId(), true);

        // When
        Boolean status = validationMapper.getValidationStatus(testEvent.getEventId(), testUser.getUserId());

        // Then
        assertNotNull(status, "Validation status should be found");
        assertTrue(status, "Validation status should be confirmed");
    }
}

