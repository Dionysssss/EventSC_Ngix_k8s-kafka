package com.steve.service;

import com.steve.dto.LoginRequest;
import com.steve.dto.LoginResponse;
import com.steve.dto.PasswordResetRequest;
import com.steve.dto.UserRegistrationRequest;
import com.steve.entity.User;
import com.steve.mapper.SecurityAnswerMapper;
import com.steve.mapper.UserMapper;
import com.steve.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SecurityAnswerMapper securityAnswerMapper;

    @Autowired
    private JwtUtil jwtUtil;

     final static private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


//    public User registerUser(User user) {
//        userMapper.insertUser(user);
//        return user;
//    }

    // Register User
    public int registerUser(UserRegistrationRequest request) {
        if (userMapper.findUserByEmail(request.getEmail()) != null) {
            throw new IllegalArgumentException("Email is already registered.");
        }

        // Hash password and save user
        User user = new User();
        user.setEmail(request.getEmail());
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(hashedPassword);
        System.out.println(hashedPassword);
//        user.setPassword(request.getPassword());
        userMapper.insertUser(user);

        // Save security answer
        if(request.getAnswer() != null){
            securityAnswerMapper.insertAnswer(user.getUserId(), request.getAnswer());
        }else{
            //throw new IllegalArgumentException("No Security Answer Set.");
        }


        return user.getUserId();
    }

    // Forget Password
    public void resetPassword(PasswordResetRequest request) {
        User user = userMapper.findUserByEmail(request.getEmail());
        if (user == null || !securityAnswerMapper.isValidAnswer(user.getUserId(), request.getAnswer())) {
            throw new IllegalArgumentException("Invalid security answer or user not found");
        }

        // Update password if answer is correct
        String hashedPassword = passwordEncoder.encode(request.getNewPassword());
//        String hashedPassword = request.getNewPassword();
        userMapper.updatePassword(user.getUserId(), hashedPassword);
    }


    public User getUserById(int id) {
        return userMapper.findUserById(id);
    }

    public LoginResponse loginUser(LoginRequest loginRequest) {
        User user = userMapper.findUserByEmail(loginRequest.getEmail());


        // System.out.println(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()));

        if (user == null || ! passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return null;
            // Invalid credentials
        }

        String token = jwtUtil.generateToken(user.getUserId());
        return new LoginResponse(String.valueOf(user.getUserId()), "Login successful", token);
    }

    // Get User By email
    public User getUserByEmail(String email){ return userMapper.findUserByEmail(email);}

    // Additional business logic methods can be added here
}
