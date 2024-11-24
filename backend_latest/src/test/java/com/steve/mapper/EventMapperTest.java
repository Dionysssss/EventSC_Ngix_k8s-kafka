package com.steve.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.steve.entity.Event;
import com.steve.dto.IOEvent;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EventMapperTest {

    @Autowired
    private EventMapper eventMapper;

    @BeforeEach
    void setUp() {
        // Clean up the database before each test
        eventMapper.deleteAllEvents();
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        eventMapper.deleteAllEvents();
    }

    @Test
    void testInsertEvent() {
        // Given
        Event event = new Event();
        event.setEventName("Test Event");
        event.setEventDescription("A description for the test event.");
        event.setEventDate(LocalDate.of(2024, 11, 25));
        event.setEventTime(LocalTime.of(10, 0));
        event.setEventCreatorId(1);

        // Set the location as a JSON object
        IOEvent.EventLocation location = new IOEvent.EventLocation();
        location.setLatitude(37.7749);
        location.setLongitude(-122.4194);
        event.setEventLocationFromObject(location);

        // When
        eventMapper.insertEvent(event);

        // Then
        assertNotNull(event.getEventId(), "Event ID should be auto-generated");
        Event insertedEvent = eventMapper.findEventById(event.getEventId());
        assertNotNull(insertedEvent, "Event should exist in the database");
        assertEquals("Test Event", insertedEvent.getEventName());
        assertEquals("A description for the test event.", insertedEvent.getEventDescription());
        assertEquals(LocalDate.of(2024, 11, 25), insertedEvent.getEventDate());
        assertEquals(LocalTime.of(10, 0), insertedEvent.getEventTime());
        assertEquals(1, insertedEvent.getEventCreatorId());

        // Verify the location
        IOEvent.EventLocation insertedLocation = insertedEvent.getEventLocationAsObject();
        assertNotNull(insertedLocation, "Event location should be deserialized successfully");
        assertEquals(37.7749, insertedLocation.getLatitude());
        assertEquals(-122.4194, insertedLocation.getLongitude());
    }

    @Test
    void testFindAllEvents() {
        // Given
        Event event1 = new Event();
        event1.setEventName("Event 1");
        event1.setEventDescription("Description 1");
        event1.setEventDate(LocalDate.of(2024, 11, 25));
        event1.setEventTime(LocalTime.of(10, 0));
        event1.setEventCreatorId(1);
        eventMapper.insertEvent(event1);

        Event event2 = new Event();
        event2.setEventName("Event 2");
        event2.setEventDescription("Description 2");
        event2.setEventDate(LocalDate.of(2024, 11, 25));
        event2.setEventTime(LocalTime.of(12, 0));
        event2.setEventCreatorId(2);
        eventMapper.insertEvent(event2);

        // When
        List<Event> events = eventMapper.findAllEvents(null, "2024-11-25");

        // Then
        assertEquals(2, events.size(), "There should be 2 events on the specified date");
    }

    @Test
    void testFindEventById() {
        // Given
        Event event = new Event();
        event.setEventName("Test Event");
        event.setEventDescription("A description for the test event.");
        event.setEventDate(LocalDate.of(2024, 11, 25));
        event.setEventTime(LocalTime.of(10, 0));
        event.setEventCreatorId(1);
        eventMapper.insertEvent(event);

        // When
        Event foundEvent = eventMapper.findEventById(event.getEventId());

        // Then
        assertNotNull(foundEvent, "Event should exist in the database");
        assertEquals("Test Event", foundEvent.getEventName());
    }

    @Test
    void testUpdateEvent() {
        // Given
        Event event = new Event();
        event.setEventName("Old Event Name");
        event.setEventDescription("Old Description");
        event.setEventDate(LocalDate.of(2024, 11, 25));
        event.setEventTime(LocalTime.of(10, 0));
        event.setEventCreatorId(1);
        eventMapper.insertEvent(event);

        // Update details
        event.setEventName("Updated Event Name");
        event.setEventDescription("Updated Description");

        // When
        int rowsAffected = eventMapper.updateEvent(event);

        // Then
        assertEquals(1, rowsAffected, "One row should be updated");
        Event updatedEvent = eventMapper.findEventById(event.getEventId());
        assertEquals("Updated Event Name", updatedEvent.getEventName());
        assertEquals("Updated Description", updatedEvent.getEventDescription());
    }

    @Test
    void testDeleteEvent() {
        // Given
        Event event = new Event();
        event.setEventName("Test Event");
        event.setEventDescription("A description for the test event.");
        event.setEventDate(LocalDate.of(2024, 11, 25));
        event.setEventTime(LocalTime.of(10, 0));
        event.setEventCreatorId(1);
        eventMapper.insertEvent(event);

        // When
        int rowsDeleted = eventMapper.deleteEvent(event.getEventId());

        // Then
        assertEquals(1, rowsDeleted, "One row should be deleted");
        Event deletedEvent = eventMapper.findEventById(event.getEventId());
        assertNull(deletedEvent, "Deleted event should no longer exist in the database");
    }
}
