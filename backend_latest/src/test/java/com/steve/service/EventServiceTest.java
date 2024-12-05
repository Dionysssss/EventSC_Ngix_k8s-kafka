package com.steve.service;

import com.steve.dto.IOEvent;
import com.steve.entity.Event;
import com.steve.entity.User;
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
public class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private UserMapper userMapper;

    // Global static User instance
    private static User testUser;

    @BeforeAll
    void setUp() {
        // Clean up the database
        eventMapper.deleteAllEvents();
        userMapper.deleteAllUsers();

        // Insert a test User
        testUser = new User();
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("password123");
        userMapper.insertUser(testUser);
    }

    @AfterAll
    void tearDown() {
        // Clean up the database
        eventMapper.deleteAllEvents();
        userMapper.deleteAllUsers();
    }

    @Test
    void testCreateEvent() {
        // Given
        IOEvent ioEvent = new IOEvent();
        ioEvent.setEventName("Test Event");
        ioEvent.setEventDescription("This is a test event.");
        ioEvent.setEventDate(LocalDate.of(2024, 12, 25));
        ioEvent.setEventTime(LocalTime.of(10, 0));
        ioEvent.setEventCreatorId(testUser.getUserId());

        // When
        IOEvent createdEvent = eventService.createEvent(ioEvent);

        // Then
        assertNotNull(createdEvent.getEventId(), "Event ID should be auto-generated");
        Event fetchedEvent = eventMapper.findEventById(createdEvent.getEventId());
        assertNotNull(fetchedEvent, "Event should be found in the database");
        assertEquals("Test Event", fetchedEvent.getEventName());
    }

    @Test
    void testGetAllEvents() {
        // Insert a test event
        IOEvent ioEvent = new IOEvent();
        ioEvent.setEventName("Another Test Event");
        ioEvent.setEventDescription("Another description.");
        ioEvent.setEventDate(LocalDate.now());
        ioEvent.setEventTime(LocalTime.now().plusHours(1));
        ioEvent.setEventCreatorId(testUser.getUserId());
        eventService.createEvent(ioEvent);

        // When
        List<IOEvent> events = eventService.getAllEvents(null, null);

        // Then
        assertFalse(events.isEmpty(), "Events list should not be empty");
        assertTrue(events.stream().anyMatch(event -> event.getEventName().equals("Another Test Event")),
                "Inserted event should be in the list");
    }

    @Test
    void testGetUpcomingEvents() {
        // Insert past and future events
        IOEvent pastEvent = new IOEvent();
        pastEvent.setEventName("Past Event");
        pastEvent.setEventDescription("Happened in the past.");
        pastEvent.setEventDate(LocalDate.now().minusDays(1));
        pastEvent.setEventTime(LocalTime.now().minusHours(2));
        pastEvent.setEventCreatorId(testUser.getUserId());
        eventService.createEvent(pastEvent);

        IOEvent futureEvent = new IOEvent();
        futureEvent.setEventName("Future Event");
        futureEvent.setEventDescription("Will happen in the future.");
        futureEvent.setEventDate(LocalDate.now().plusDays(1));
        futureEvent.setEventTime(LocalTime.now().plusHours(2));
        futureEvent.setEventCreatorId(testUser.getUserId());
        eventService.createEvent(futureEvent);

        // When
        List<IOEvent> upcomingEvents = eventService.getUpcomingEvents(null);

        // Then
        assertFalse(upcomingEvents.isEmpty(), "Upcoming events list should not be empty");
        assertTrue(upcomingEvents.stream().anyMatch(event -> event.getEventName().equals("Future Event")),
                "Future event should be in the upcoming events list");
        assertTrue(upcomingEvents.stream().noneMatch(event -> event.getEventName().equals("Past Event")),
                "Past event should not be in the upcoming events list");
    }

    @Test
    void testEditEvent() {
        // Insert a test event
        IOEvent ioEvent = new IOEvent();
        ioEvent.setEventName("Editable Event");
        ioEvent.setEventDescription("Original description.");
        ioEvent.setEventDate(LocalDate.now());
        ioEvent.setEventTime(LocalTime.now());
        ioEvent.setEventCreatorId(testUser.getUserId());
        IOEvent createdEvent = eventService.createEvent(ioEvent);

        // Update the event
        IOEvent updatedEvent = new IOEvent();
        updatedEvent.setEventId(createdEvent.getEventId());
        updatedEvent.setEventName("Updated Event Name");
        updatedEvent.setEventDescription("Updated description.");
        updatedEvent.setEventDate(LocalDate.now());
        updatedEvent.setEventTime(LocalTime.now());
        updatedEvent.setEventCreatorId(testUser.getUserId());

        // When
        boolean result = eventService.editEvent(createdEvent.getEventId(), updatedEvent);

        // Then
        assertTrue(result, "Event should be updated successfully");
        Event fetchedEvent = eventMapper.findEventById(createdEvent.getEventId());
        assertEquals("Updated Event Name", fetchedEvent.getEventName());
    }

    @Test
    void testDeleteEvent() {
        // Insert a test event
        IOEvent ioEvent = new IOEvent();
        ioEvent.setEventName("Deletable Event");
        ioEvent.setEventDescription("Will be deleted.");
        ioEvent.setEventDate(LocalDate.now());
        ioEvent.setEventTime(LocalTime.now());
        ioEvent.setEventCreatorId(testUser.getUserId());
        IOEvent createdEvent = eventService.createEvent(ioEvent);

        // When
        boolean result = eventService.deleteEvent(createdEvent.getEventId(), testUser.getUserId());

        // Then
        assertTrue(result, "Event should be deleted successfully");
        Event fetchedEvent = eventMapper.findEventById(createdEvent.getEventId());
        assertNull(fetchedEvent, "Event should not exist in the database");
    }
}
