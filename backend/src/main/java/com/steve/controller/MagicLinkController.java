package com.steve.controller;

import com.steve.entity.User;
import com.steve.service.MagicLinkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class MagicLinkController {

    private final MagicLinkService magicLinkService;

    public MagicLinkController(MagicLinkService magicLinkService) {
        this.magicLinkService = magicLinkService;
    }

    @PostMapping("/send-magic-link")
    public ResponseEntity<String> sendMagicLink(@RequestBody User user) {
        try {
            magicLinkService.sendMagicLink(user.getEmail());
            return ResponseEntity.ok("Magic link sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to send magic link: " + e.getMessage());
        }
    }

    @PostMapping("/verify-magic-link")
    public ResponseEntity<String> verifyMagicLink(@RequestParam String token) {
        try {
            User user = magicLinkService.verifyToken(token);
            return ResponseEntity.ok("Magic link verified. User logged in: " + user.getEmail());
            // Optionally, return a JWT or session information if needed
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired magic link.");
        }
    }
}
