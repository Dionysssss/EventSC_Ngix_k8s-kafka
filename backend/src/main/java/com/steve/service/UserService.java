package com.steve.service;

import com.steve.dto.LoginRequest;
import com.steve.dto.LoginResponse;
import com.steve.entity.User;
import com.steve.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User registerUser(User user) {
        userMapper.insertUser(user);
        return user;
    }

    public User getUserById(int id) {
        return userMapper.findUserById(id);
    }

    public LoginResponse loginUser(LoginRequest loginRequest) {
        User user = userMapper.findUserByEmail(loginRequest.getEmail());

        if (user == null || !user.getPassword().equals(loginRequest.getPassword())) {
            return null;
            // Invalid credentials
        }

        // Generate token (placeholder for JWT or other token generation)
        String token = "sample-token"; // Replace with actual token generation logic

        return new LoginResponse(String.valueOf(user.getUserId()), "Login successful");
    }

    // Additional business logic methods can be added here
}
