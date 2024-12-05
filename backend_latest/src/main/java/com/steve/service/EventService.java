package com.steve.service;

import com.steve.dto.IOEvent;
import com.steve.entity.Event;
import com.steve.mapper.EventMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventMapper eventMapper;

    public IOEvent createEvent(IOEvent ioEvent) {
        Event event = convertToEntity(ioEvent);
        eventMapper.insertEvent(event);
        ioEvent.setEventId(event.getEventId());
        return ioEvent;
    }

    // Get All Events
    public List<IOEvent> getAllEvents(String category, String date) {
        List<Event> events = eventMapper.findAllEvents(category, date);
        return events.stream().map(this::convertToIOEvent).collect(Collectors.toList());
    }

    // Get Upcoming Events
    public List<IOEvent> getUpcomingEvents(String category) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        List<Event> events = eventMapper.findAllEvents(category, null); // Retrieve events without a date filter

        // Filter and map events to IOEvent objects
        return events.stream()
                .filter(event ->
                        event.getEventDate().isAfter(today) || // Events after today
                        (event.getEventDate().isEqual(today) && event.getEventTime().isAfter(now) ) // Today's events but later than now
                )
                .map(this::convertToIOEvent)
                .collect(Collectors.toList());
    }

    public Event getEventById(int id) {
        return eventMapper.findEventById(id);
    }

    // Update Events
    public boolean editEvent(int eventId, IOEvent ioEvent) {
        // Retrieve the existing event to check authorization
        Event existingEvent = eventMapper.findEventById(eventId);
        if (existingEvent == null) {
            System.out.println("Event not found for ID: " + eventId);
            return false;
        }
        if (existingEvent.getEventCreatorId() != ioEvent.getEventCreatorId()) {
            System.out.println("Authorization failed: Existing creator ID (" + existingEvent.getEventCreatorId() +
                    ") does not match request creator ID (" + ioEvent.getEventCreatorId() + ")");
            return false; // Not authorized
        }

        // Convert IOEvent to Event entity and serialize eventLocation
        Event updatedEvent = convertToEntity(ioEvent);
        updatedEvent.setEventId(eventId); // Ensure we update the correct event
        updatedEvent.setEventLocationFromObject(ioEvent.getEventLocation()); // Serialize eventLocation to JSON

        // Perform the update
        int rowsAffected = eventMapper.updateEvent(updatedEvent);
        return rowsAffected > 0;
    }


    public Event convertToEntity(IOEvent ioEvent) {
        Event event = new Event();
        event.setEventId(ioEvent.getEventId());
        event.setEventName(ioEvent.getEventName());
        event.setEventDescription(ioEvent.getEventDescription());
        event.setEventDate(ioEvent.getEventDate());
        event.setEventTime(ioEvent.getEventTime());
        event.setEventCreatorId(ioEvent.getEventCreatorId());
        event.setEventLocationFromObject(ioEvent.getEventLocation());
        return event;
    }

    public IOEvent convertToIOEvent(Event event) {
        IOEvent ioEvent = new IOEvent();
        ioEvent.setEventId(event.getEventId());
        ioEvent.setEventName(event.getEventName());
        ioEvent.setEventDescription(event.getEventDescription());
        ioEvent.setEventDate(event.getEventDate());
        ioEvent.setEventTime(event.getEventTime());
        ioEvent.setEventCreatorId(event.getEventCreatorId());
        ioEvent.setEventLocation(event.getEventLocationAsObject());
        return ioEvent;
    }

    //Delete Event
    public boolean deleteEvent(int eventId, int userId) {
        // Verify that the userId is the creator of the event
        Event event = eventMapper.findEventById(eventId);
        if (event == null) {
            return false; // Event not found
        }

        if (event.getEventCreatorId() != userId) {
            return false; // User is not authorized to delete this event
        }

        // Proceed with deletion
        int rowsAffected = eventMapper.deleteEvent(eventId);
        return rowsAffected > 0;
    }

}
