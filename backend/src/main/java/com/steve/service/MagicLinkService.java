package com.steve.service;

import com.steve.entity.User;
import com.steve.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class MagicLinkService {
    private final JavaMailSender mailSender;
    private final UserMapper userMapper;

    @Value("${app.magic-link.base-url}")
    private String baseUrl; // URL of your frontend, where the magic link will redirect

    public MagicLinkService(JavaMailSender mailSender, UserMapper userMapper) {
        this.mailSender = mailSender;
        this.userMapper = userMapper;
    }

    public void sendMagicLink(String email) throws Exception {
        User user = userMapper.findUserByEmail(email);
        if (user == null) {
            throw new Exception("User not found");
        }

        // Generate a unique token and save to user
        String token = UUID.randomUUID().toString();
        user.setMagicLinkToken(token);
        userMapper.updateUserToken(email, token); // Save token to the database

        // Construct and send the magic link
        String magicLinkUrl = baseUrl + "/auth/verify-magic-link?token=" + token;
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Your Magic Link");
        mailMessage.setText("Click the link to log in: " + magicLinkUrl);
        mailSender.send(mailMessage);
    }

    public User verifyToken(String token) throws Exception {
        User user = userMapper.findByMagicLinkToken(token);
        if (user == null || !token.equals(user.getMagicLinkToken())) {
            throw new Exception("Invalid or expired token");
        }

        user.setAuthenticated(true);
        user.setMagicLinkToken(null); // Clear token after verification
        userMapper.insertUser(user); // Save authentication status

        return user;
    }

    private String generateToken(User user) {
        return UUID.randomUUID().toString(); // Unique and unpredictable
    }
}
