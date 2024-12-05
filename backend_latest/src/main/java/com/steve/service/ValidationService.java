package com.steve.service;

import com.steve.mapper.ValidationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class ValidationService {

    @Autowired
    private ValidationMapper validationMapper;
    @Autowired
    private  UserService userService;

    public int confirmEvent(int eventId, int userId) {
        if (validationMapper.findValidation(eventId, userId) != null) {
            throw new IllegalArgumentException("You have already validated this event");
        }
        validationMapper.insertValidation(eventId, userId, true);
        return validationMapper.countConfirmed(eventId);
    }

    public int withdrawConfirmation(int eventId, int userId) {
        if (validationMapper.deleteValidation(eventId, userId, true) == 0) {
            throw new IllegalArgumentException("No confirmation found for this event by the user");
        }
        return validationMapper.countConfirmed(eventId);
    }

    public int reportEventAsFalse(int eventId, int userId) {
        if (validationMapper.findValidation(eventId, userId) != null) {
            throw new IllegalArgumentException("You have already validated this event");
        }
        validationMapper.insertValidation(eventId, userId, false);
        return validationMapper.countFalseReports(eventId);
    }

    public int withdrawFalseReport(int eventId, int userId) {
        if (validationMapper.deleteValidation(eventId, userId, false) == 0) {
            throw new IllegalArgumentException("No false report found for this event by the user");
        }
        return validationMapper.countFalseReports(eventId);
    }

    public Map<String, Integer> getValidationCounts(int eventId) {
        int confirmedCount = validationMapper.countConfirmed(eventId);
        int falseReportsCount = validationMapper.countFalseReports(eventId);

        Map<String, Integer> counts = new HashMap<>();
        counts.put("confirmedCount", confirmedCount);
        counts.put("falseReportsCount", falseReportsCount);
        return counts;
    }

    // getValidationStatus
    public String getValidationStatus(int eventId, int userId) {

        if(userService.getUserById(userId) == null){
            throw new IllegalArgumentException("No User Found");
        }

        Boolean isConfirmed = validationMapper.getValidationStatus(eventId, userId);

        if (isConfirmed == null) {

            return "notReport";  // User has not validated this event
        } else if (isConfirmed) {
            return "confirmed";  // User has confirmed this event
        } else {
            return "falseReport";  // User has reported this event as false
        }
    }
}
