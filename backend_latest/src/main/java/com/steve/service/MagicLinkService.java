package com.steve.service;

import com.steve.entity.User;
import com.steve.entity.VerificationToken;
import com.steve.mapper.UserMapper;
import com.steve.mapper.VerificationTokenMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;


@Service
public class MagicLinkService {
    @Autowired
    private static final Logger LOGGER = Logger.getLogger(MagicLinkService.class.getName());

    private final JavaMailSender mailSender;
    private final UserMapper userMapper;
    private final VerificationTokenMapper verificationTokenMapper;

    @Value("${app.magic-link.base-url}")
    private String baseUrl;

    @Value("${spring.mail.username}")
    private String emailSender;  // Email sender address

    public MagicLinkService(JavaMailSender mailSender, UserMapper userMapper, VerificationTokenMapper verificationTokenMapper) {
        this.mailSender = mailSender;
        this.userMapper = userMapper;
        this.verificationTokenMapper = verificationTokenMapper;
    }

//    public void sendMagicLink(String email) throws Exception {
//        LOGGER.info("Attempting to send magic link to email: " + email);
//        User user = userMapper.findUserByEmail(email);
//        if (user == null) {
//            LOGGER.warning("User not found for email: " + email);
//            throw new Exception("User not found");
//        }
//
//        // Generate token and save it in the verification table
//        String token = UUID.randomUUID().toString();
//        VerificationToken verificationToken = new VerificationToken(token, user.getUserId(), LocalDateTime.now().plusMinutes(15));
//        verificationTokenMapper.insertVerificationToken(verificationToken);
//
//        // Prepare and send the email
//        String magicLinkUrl = baseUrl + "/auth/verify-magic-link?token=" + token;
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setFrom(emailSender);// Set the sender email
//        mailMessage.setSubject("Your Magic Link");
//        mailMessage.setTo(email);
//        mailMessage.setText("Click the link to log in: " + magicLinkUrl);
//        mailSender.send(mailMessage);
//
//        LOGGER.info("Magic link email sent successfully to: " + email);
//    }

    public void sendVerificationCode(String email) throws Exception {
        LOGGER.info("Attempting to send verification code to email: " + email);
        User user = userMapper.findUserByEmail(email);
        if (user == null) {
            LOGGER.warning("User not found for email: " + email);
            throw new Exception("User not found");
        }

        // Generate a 6-digit numeric code
        String code = String.format("%06d", new Random().nextInt(1000000));

        // Save the code with expiration in the VerificationToken table
        VerificationToken verificationToken = new VerificationToken(code, user.getUserId(), LocalDateTime.now().plusMinutes(15));
        verificationTokenMapper.insertVerificationToken(verificationToken);

        // Prepare and send the email
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(emailSender);
        mailMessage.setSubject("Your Verification Code");
        mailMessage.setTo(email);
        mailMessage.setText("Your verification code is: " + code);
        mailSender.send(mailMessage);

        LOGGER.info("Verification code sent successfully to: " + email);
    }

//    public boolean verifyToken(String token) {
//        LOGGER.info("Attempting to verify token: " + token);
//
//        VerificationToken verificationToken = verificationTokenMapper.findByToken(token);
//        if (verificationToken == null) {
//            LOGGER.warning("No verification token found for token: " + token);
//            return false;
//        }
//
//        if (verificationToken.getExpiration().isBefore(LocalDateTime.now())) {
//            LOGGER.warning("Token expired for token: " + token);
//            return false;
//        }
//
//        LOGGER.info("Token verified successfully for user ID: " + verificationToken.getUserId());
//        return true;
//    }

    public boolean verifyCode(String code, int userId) {
        LOGGER.info("Attempting to verify code: " + code + " for user ID: " + userId);

        VerificationToken verificationToken = verificationTokenMapper.findByCodeAndUserId(code, userId);
        if (verificationToken == null) {
            LOGGER.warning("No verification token found for code: " + code);
            return false;
        }

        if (verificationToken.getExpiration().isBefore(LocalDateTime.now())) {
            LOGGER.warning("Code expired for code: " + code);
            return false;
        }

        LOGGER.info("Code verified successfully for user ID: " + verificationToken.getUserId());
        return true;
    }

//    public static Map<String, String> convertStringToMap(String input) {
//        Map<String, String> map = new HashMap<>();
//
//        try {
//            // Parse the input string as JSON
//            JSONObject jsonObject = new JSONObject(input);
//            Iterator<String> keys = jsonObject.keys();
//
//            // Populate the map with keys and values from the JSON object
//            while (keys.hasNext()) {
//                String key = keys.next();
//                map.put(key, jsonObject.getString(key));
//            }
//        } catch (Exception e) {
//            System.err.println("Failed to parse input string: " + e.getMessage());
//        }
//
//        return map;
//    }

    public static Map<String, String> convertStringToMap(String input) {
        Map<String, String> map = new HashMap<>();
        int firstQuoteIndex = input.indexOf('"');
        String result = input.substring(firstQuoteIndex);
        result = result.replaceAll("}$", "");
        result = result.replace("\"", "");
        String[] keyValuePairs = result.split(",");
        for (String pair : keyValuePairs) {
            String[] entry = pair.split(":");
            if (entry.length > 1) {
                map.put(entry[0], entry[1]);
            } else {
                map.put(entry[0], "");
            }
        }
        return map;
    }
}