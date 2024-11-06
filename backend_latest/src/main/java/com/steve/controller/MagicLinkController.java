//package com.steve.controller;
//
//import com.steve.service.MagicLinkService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//import static com.steve.service.MagicLinkService.convertStringToMap;
//
//@RestController
//@RequestMapping("/auth")
//public class MagicLinkController {
//
//    private final MagicLinkService magicLinkService;
//
//    public MagicLinkController(MagicLinkService magicLinkService) {
//        this.magicLinkService = magicLinkService;
//    }
//
//    @PostMapping("/send-magic-link")
//    public ResponseEntity<String> sendMagicLink(@RequestBody String request) {
//        Map<String, String> map = convertStringToMap(request);
//        String email = map.get("email");
//
//        try {
//            magicLinkService.sendMagicLink(email);
//            return ResponseEntity.ok("Magic link sent successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to send magic link: " + e.getMessage());
//        }
//    }
//
//    @GetMapping("/verify-magic-link")
//    public ResponseEntity<String> verifyMagicLink(@RequestParam String token) {
//        try {
//            boolean verified = magicLinkService.verifyToken(token);
//            return verified
//                    ? ResponseEntity.ok("Magic link verified. User logged in.")
//                    : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired magic link.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Verification failed: " + e.getMessage());
//        }
//    }
//}

package com.steve.controller;

import com.steve.service.MagicLinkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.steve.service.MagicLinkService.convertStringToMap;

@RestController
@RequestMapping("/auth")
public class MagicLinkController {

    private final MagicLinkService verificationCodeService;

    public MagicLinkController(MagicLinkService verificationCodeService) {
        this.verificationCodeService = verificationCodeService;
    }

    // Endpoint to send a verification code via email
    @PostMapping("/send-magic-link")
    public ResponseEntity<String> sendVerificationCode(@RequestBody String request) {
        Map<String, String> map = convertStringToMap(request);
        String email = map.get("email");

        try {
            verificationCodeService.sendVerificationCode(email);
            String s = "{\"success\":[{\"result\":\"Verification code sent successfully.\"}]}";
            return ResponseEntity.ok(s);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to send verification code: " + e.getMessage());
        }
    }

    // Endpoint to verify the code and proceed with password reset
    @GetMapping("/verify-magic-link")
    public ResponseEntity<String> verifyCode(@RequestBody String request) {
        Map<String, String> map = convertStringToMap(request);
        String code = map.get("code");
        int userId;

        try {
            userId = Integer.parseInt(map.get("userId"));
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid userId format.");
        }

        try {
            boolean verified = verificationCodeService.verifyCode(code, userId);
            if (verified) {
                return ResponseEntity.ok("Verification successful. Proceed with password reset.");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired verification code.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Verification failed: " + e.getMessage());
        }
    }
}