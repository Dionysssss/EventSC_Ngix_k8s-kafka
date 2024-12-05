package com.steve.service;

import com.steve.dto.LoginRequest;
import com.steve.dto.LoginResponse;
import com.steve.dto.PasswordResetRequest;
import com.steve.dto.UserRegistrationRequest;
import com.steve.entity.User;
import com.steve.mapper.SecurityAnswerMapper;
import com.steve.mapper.UserMapper;
import com.steve.utils.TestLogger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // Loads the full Spring application context
@Transactional // Ensures tests are rolled back after execution
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SecurityAnswerMapper securityAnswerMapper;

    private int testUserId;

    @BeforeEach
    void setUp() {
        // Ensure the database is clean before each test
        userMapper.deleteAllUsers(); // Add this method in your UserMapper for cleanup
//        securityAnswerMapper.deleteAllAnswers(); // Add this method for cleanup
    }

    @AfterEach
    void tearDown() {
        // Clean up created data after tests
        if (testUserId != 0) {
            userMapper.deleteUserById(testUserId);
        }
    }

    @Test
    void testRegisterUser() {
        // Given
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setAnswer("Test Answer");

        // When
        testUserId = userService.registerUser(request);

        // Then
        User registeredUser = userMapper.findUserById(testUserId);
        assertNotNull(registeredUser);
        assertEquals("test@example.com", registeredUser.getEmail());
        assertEquals("password123", registeredUser.getPassword());

    }

    @Test
    void testResetPassword() {
        // Given
        UserRegistrationRequest registerRequest = new UserRegistrationRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setAnswer("Test Answer");

        // Register the user
        testUserId = userService.registerUser(registerRequest);

        // Reset password
        PasswordResetRequest resetRequest = new PasswordResetRequest();
        resetRequest.setEmail("test@example.com");
        resetRequest.setAnswer("Test Answer");
        resetRequest.setNewPassword("newPassword123");

        // When
        userService.resetPassword(resetRequest);

        // Then
        User updatedUser = userMapper.findUserById(testUserId);
        assertNotNull(updatedUser);
        assertEquals("newPassword123", updatedUser.getPassword());
    }

    @Test
    void testLoginUser() {
        // Given
        UserRegistrationRequest registerRequest = new UserRegistrationRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setAnswer("Test Answer");

        // Register the user
        testUserId = userService.registerUser(registerRequest);

        // Login with valid credentials
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        // When
        LoginResponse response = userService.loginUser(loginRequest);

        // Then
        assertNotNull(response);
        assertEquals(String.valueOf(testUserId), response.getUserId());
        assertEquals("Login successful", response.getMessage());
    }

    @Test
    void testGetUserById() {
        // Given
        UserRegistrationRequest registerRequest = new UserRegistrationRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setAnswer("Test Answer");

        // Register the user
        testUserId = userService.registerUser(registerRequest);

        // When
        User user = userService.getUserById(testUserId);

        // Then
        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void testGetUserByEmail() {
        // Given
        UserRegistrationRequest registerRequest = new UserRegistrationRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setAnswer("Test Answer");

        // Register the user
        testUserId = userService.registerUser(registerRequest);

        // When
        User user = userService.getUserByEmail("test@example.com");

        // Then
        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
    }
}
