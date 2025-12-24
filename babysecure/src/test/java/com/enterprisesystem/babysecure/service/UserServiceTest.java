package com.enterprisesystem.babysecure.service;

import com.enterprisesystem.babysecure.model.User;
import com.enterprisesystem.babysecure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit test for UserService
 */
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        // Given
        User user = new User("testuser", "test@example.com", "password123");
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User createdUser = userService.createUser(user);

        // Then
        assertNotNull(createdUser);
        assertEquals("testuser", createdUser.getUsername());
        assertEquals("test@example.com", createdUser.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testFindUserById() {
        // Given
        User user = new User("testuser", "test@example.com", "password123");
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        User foundUser = userService.findById(1L);

        // Then
        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
        assertEquals("testuser", foundUser.getUsername());
    }

    @Test
    void testUpdateUserStatus() {
        // Given
        User user = new User("testuser", "test@example.com", "password123");
        user.setId(1L);
        user.setEnabled(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User updatedUser = userService.updateUserStatus(1L, false);

        // Then
        assertNotNull(updatedUser);
        assertEquals(false, updatedUser.getEnabled());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUsernameExists() {
        // Given
        when(userRepository.existsByUsername("existinguser")).thenReturn(true);
        when(userRepository.existsByUsername("newuser")).thenReturn(false);

        // When & Then
        assertTrue(userService.usernameExists("existinguser"));
        assertFalse(userService.usernameExists("newuser"));
    }

    @Test
    void testEmailExists() {
        // Given
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);

        // When & Then
        assertTrue(userService.emailExists("existing@example.com"));
        assertFalse(userService.emailExists("new@example.com"));
    }
}
