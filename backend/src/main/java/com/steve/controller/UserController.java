package com.steve.controller;

import com.steve.dto.ErrorResponse;
import com.steve.dto.LoginRequest;
import com.steve.dto.LoginResponse;
import com.steve.dto.RegistrationResponse;
import com.steve.entity.User;
import com.steve.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> registerUser(@RequestBody User user) {
        userService.registerUser(user);
        System.out.println(user);
        // Create the response object
        RegistrationResponse response = new RegistrationResponse(
                String.valueOf(user.getUserId()),
                "Registration successful"
        );
        return ResponseEntity.ok(response);

    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid LoginRequest loginRequest, BindingResult result) {

        if (result.hasErrors()) {
            String message = result.getFieldError().getDefaultMessage();
            ErrorResponse errorResponse = new ErrorResponse(400, message);
            return ResponseEntity.status(400).body(errorResponse);
        }
        LoginResponse response = userService.loginUser(loginRequest);
        if (response == null) {
            ErrorResponse errorResponse = new ErrorResponse(401, "Incorrect email or password.");
            return ResponseEntity.status(401).body(errorResponse);
        }

        System.out.println("User: " + response.getUserId() + " log-in successfully");
        return ResponseEntity.ok(response);
    }


    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }



    // Additional methods (e.g., login, update user, delete user) can be added here
}
