package com.steve.mapper;

import com.steve.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("classpath:application-test.properties")
@MybatisTest // Bootstraps MyBatis with an embedded database for testing
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        // Ensure the database is empty before each test
        userMapper.deleteAllUsers();
    }

    @AfterEach
    void tearDown() {
        // Clean up the database after each test
        userMapper.deleteAllUsers();
    }

    @Test
    void testInsertUser() {
        // Given
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");

        // When
        userMapper.insertUser(user);

        // Then
        assertNotNull(user.getUserId(), "User ID should be auto-generated");
        User insertedUser = userMapper.findUserById(user.getUserId());
        assertNotNull(insertedUser, "User should exist in the database");
        assertEquals("test@example.com", insertedUser.getEmail());
        assertEquals("password123", insertedUser.getPassword());
    }

    @Test
    void testFindUserById() {
        // Given
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        userMapper.insertUser(user);

        // When
        User foundUser = userMapper.findUserById(user.getUserId());

        // Then
        assertNotNull(foundUser, "User should exist in the database");
        assertEquals(user.getUserId(), foundUser.getUserId());
        assertEquals(user.getEmail(), foundUser.getEmail());
        assertEquals(user.getPassword(), foundUser.getPassword());
    }

    @Test
    void testFindUserByEmail() {
        // Given
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        userMapper.insertUser(user);

        // When
        User foundUser = userMapper.findUserByEmail("test@example.com");

        // Then
        assertNotNull(foundUser, "User should exist in the database");
        assertEquals(user.getEmail(), foundUser.getEmail());
        assertEquals(user.getPassword(), foundUser.getPassword());
    }

    @Test
    void testUpdatePassword() {
        // Given
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("oldPassword");
        userMapper.insertUser(user);

        // When
        userMapper.updatePassword(user.getUserId(), "newPassword");

        // Then
        User updatedUser = userMapper.findUserById(user.getUserId());
        assertNotNull(updatedUser, "User should still exist in the database");
        assertEquals("newPassword", updatedUser.getPassword());
    }

    @Test
    void testDeleteUserById() {
        // Given
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        userMapper.insertUser(user);

        // When
        userMapper.deleteUserById(user.getUserId());

        // Then
        User deletedUser = userMapper.findUserById(user.getUserId());
        assertNull(deletedUser, "User should no longer exist in the database");
    }

    @Test
    void testDeleteAllUsers() {
        // Given
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setPassword("password1");
        userMapper.insertUser(user1);

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setPassword("password2");
        userMapper.insertUser(user2);

        // When
        userMapper.deleteAllUsers();

        // Then
        List<User> allUsers = userMapper.findAllUsers();
        assertTrue(allUsers.isEmpty(), "All users should be deleted from the database");
    }
}
