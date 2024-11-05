package com.steve.controller;

import com.steve.dto.*;
import com.steve.entity.User;
import com.steve.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

//    // Register a new user
//    @PostMapping("/register")
//    public ResponseEntity<RegistrationResponse> registerUser(@RequestBody User user) {
//        userService.registerUser(user);
//        System.out.println(user);
//        // Create the response object
//        RegistrationResponse response = new RegistrationResponse(
//                String.valueOf(user.getUserId()),
//                "Registration successful"
//        );
//        return ResponseEntity.ok(response);
//
//    }

    // Registration Endpoint
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest request) {
        try {
            int userId = userService.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new RegistrationResponse("Registration successful", String.valueOf(userId)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(409, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "Invalid request"));
        }
    }

    // Forget Password Endpoint
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request) {
        try {
            userService.resetPassword(request);
            return ResponseEntity.ok(new ResponseMessage("Password reset successful"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid LoginRequest loginRequest, BindingResult result) {

        if (result.hasErrors()) {
            String message = Objects.requireNonNull(result.getFieldError()).getDefaultMessage();
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
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);

        if (user == null){
            ErrorResponse errorResponse = new ErrorResponse(404, "User Not Found");
            return ResponseEntity.status(404).body(errorResponse);
        }

        return ResponseEntity.ok(user);

    }

    // Get User By Email
    @GetMapping()
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
        User user = userService.getUserByEmail(email);

        if (user == null){
            ErrorResponse errorResponse = new ErrorResponse(404, "User Not Found");
            return ResponseEntity.status(404).body(errorResponse);
        }

        return ResponseEntity.ok(user);

    }



    // Additional methods (e.g., login, update user, delete user) can be added here
    public class ResponseMessage{
        String message;

        public ResponseMessage(){}
        public ResponseMessage(String message) { this.message = message; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
