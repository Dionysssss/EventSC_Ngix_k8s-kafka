package com.steve.mapper;

import com.steve.entity.User;
import com.steve.entity.VerificationToken;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VerificationTokenMapperTest {

    @Autowired
    private VerificationTokenMapper verificationTokenMapper;

    @Autowired
    private UserMapper userMapper;

    // Global static User instance
    private static User testUser;

    @BeforeAll
    void setUp() {
        // Clean up database
        verificationTokenMapper.deleteVerificationToken(1);
        userMapper.deleteAllUsers();

        // Insert a test User
        testUser = new User();
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("password123");
        userMapper.insertUser(testUser);
    }

    @AfterAll
    void tearDown() {
        // Clean up database
        verificationTokenMapper.deleteVerificationToken(testUser.getUserId());
        userMapper.deleteAllUsers();
    }

    @Test
    void testInsertVerificationToken() {
        // Given
        VerificationToken token = new VerificationToken();
        token.setToken("testToken123");
        token.setUserId(testUser.getUserId());
        token.setExpiration(LocalDateTime.now().plusHours(1));

        // When
        verificationTokenMapper.insertVerificationToken(token);

        // Then
        assertNotNull(token.getId(), "Token ID should be auto-generated");
        VerificationToken fetchedToken = verificationTokenMapper.findByToken("testToken123");
        assertNotNull(fetchedToken, "Token should exist in the database");
        assertEquals(testUser.getUserId(), fetchedToken.getUserId());
        assertEquals("testToken123", fetchedToken.getToken());
    }

    @Test
    void testFindByToken() {
        // Insert a token
        VerificationToken token = new VerificationToken();
        token.setToken("testTokenFind");
        token.setUserId(testUser.getUserId());
        token.setExpiration(LocalDateTime.now().plusHours(1));
        verificationTokenMapper.insertVerificationToken(token);

        // When
        VerificationToken fetchedToken = verificationTokenMapper.findByToken("testTokenFind");

        // Then
        assertNotNull(fetchedToken, "Token should be fetched from the database");
        assertEquals(testUser.getUserId(), fetchedToken.getUserId());
        assertEquals("testTokenFind", fetchedToken.getToken());
    }

    @Test
    void testFindByCodeAndUserId() {
        // Insert a token
        VerificationToken token = new VerificationToken();
        token.setToken("testTokenCode");
        token.setUserId(testUser.getUserId());
        token.setExpiration(LocalDateTime.now().plusHours(1));
        verificationTokenMapper.insertVerificationToken(token);

        // When
        VerificationToken fetchedToken = verificationTokenMapper.findByCodeAndUserId("testTokenCode", testUser.getUserId());

        // Then
        assertNotNull(fetchedToken, "Token should be fetched by token and userId");
        assertEquals("testTokenCode", fetchedToken.getToken());
    }

    @Test
    void testDeleteVerificationToken() {
        // Insert a token
        VerificationToken token = new VerificationToken();
        token.setToken("testTokenDelete");
        token.setUserId(testUser.getUserId());
        token.setExpiration(LocalDateTime.now().plusHours(1));
        verificationTokenMapper.insertVerificationToken(token);

        // When
        verificationTokenMapper.deleteVerificationToken(testUser.getUserId());

        // Then
        VerificationToken deletedToken = verificationTokenMapper.findByToken("testTokenDelete");
        assertNull(deletedToken, "Token should be deleted from the database");
    }

    @Test
    void testDeleteExpiredTokens() {
        // Insert an expired token
        VerificationToken expiredToken = new VerificationToken();
        expiredToken.setToken("expiredToken");
        expiredToken.setUserId(testUser.getUserId());
        expiredToken.setExpiration(LocalDateTime.now().minusHours(1));
        verificationTokenMapper.insertVerificationToken(expiredToken);

        // Insert a valid token
        VerificationToken validToken = new VerificationToken();
        validToken.setToken("validToken");
        validToken.setUserId(testUser.getUserId());
        validToken.setExpiration(LocalDateTime.now().plusHours(1));
        verificationTokenMapper.insertVerificationToken(validToken);

        // When
        verificationTokenMapper.deleteExpiredTokens();

        // Then
        VerificationToken fetchedExpiredToken = verificationTokenMapper.findByToken("expiredToken");
        VerificationToken fetchedValidToken = verificationTokenMapper.findByToken("validToken");

        assertNull(fetchedExpiredToken, "Expired token should be deleted");
        assertNotNull(fetchedValidToken, "Valid token should not be deleted");
    }
}
