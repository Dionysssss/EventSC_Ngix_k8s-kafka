package com.steve.service;

import com.steve.entity.Event;
import com.steve.mapper.EventMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventMapper eventMapper;

    public Event createEvent(Event event) {
        eventMapper.insertEvent(event);
        return event; // Returns the event with the generated eventId
    }

    public List<Event> getAllEvents(String category, String date) {
        return eventMapper.findAllEvents(category, date);
    }

    public Event getUserById(int id) {
        return eventMapper.findUserById(id);
    }
}
