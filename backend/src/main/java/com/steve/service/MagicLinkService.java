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

    public void sendMagicLink(String email) {
        User user = userMapper.findUserByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        String token = generateToken(user); // Generate a unique token, e.g., UUID
        String link = baseUrl + "/verify?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your Magic Login Link");
        message.setText("Click the link below to log in:\n" + link);

        mailSender.send(message);

        user.setMagicLinkToken(token);
        userMapper.updateUserToken(email, token);
    }

    public User verifyToken(String token) {
        User user = userMapper.findByMagicLinkToken(token);

        if (user == null) {
            throw new RuntimeException("Invalid or expired token");
        }

        return user;
    }

    private String generateToken(User user) {
        return UUID.randomUUID().toString(); // Unique and unpredictable
    }
}
