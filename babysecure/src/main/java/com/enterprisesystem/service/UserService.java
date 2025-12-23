package com.enterprisesystem.service;

import com.enterprisesystem.model.User;
import com.enterprisesystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * User service implementing business logic
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Create a new user
     *
     * @param user the user to create
     * @return the created user
     */
    public User createUser(User user) {
        validateUserData(user);
        return userRepository.save(user);
    }

    /**
     * Update an existing user
     *
     * @param id the user ID
     * @param user the updated user data
     * @return the updated user
     */
    public User updateUser(Long id, User user) {
        User existingUser = findById(id);
        updateUserFields(existingUser, user);
        return userRepository.save(existingUser);
    }

    /**
     * Get user by ID
     *
     * @param id the user ID
     * @return the user
     */
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    /**
     * Get all users with pagination
     *
     * @param pageable pagination parameters
     * @return page of users
     */
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * Get all users without pagination
     *
     * @return list of all users
     */
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Delete user by ID
     *
     * @param id the user ID
     */
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * Find user by username
     *
     * @param username the username
     * @return the user
     */
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    /**
     * Find user by email
     *
     * @param email the email
     * @return the user
     */
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    /**
     * Check if username exists
     *
     * @param username the username
     * @return true if exists
     */
    @Transactional(readOnly = true)
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Check if email exists
     *
     * @param email the email
     * @return true if exists
     */
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Enable or disable user
     *
     * @param id the user ID
     * @param enabled the enabled status
     * @return the updated user
     */
    public User updateUserStatus(Long id, Boolean enabled) {
        User user = findById(id);
        user.setEnabled(enabled);
        return userRepository.save(user);
    }

    /**
     * Get all enabled users with pagination
     *
     * @param pageable pagination parameters
     * @return page of enabled users
     */
    @Transactional(readOnly = true)
    public Page<User> findAllEnabled(Pageable pageable) {
        return userRepository.findAllEnabled(pageable);
    }

    /**
     * Get all enabled users
     *
     * @return list of enabled users
     */
    @Transactional(readOnly = true)
    public List<User> findAllEnabled() {
        return userRepository.findAllEnabled();
    }

    /**
     * Find users by name
     *
     * @param name the name to search
     * @param pageable pagination parameters
     * @return page of users matching the name
     */
    @Transactional(readOnly = true)
    public Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable) {
        return userRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    /**
     * Count enabled users
     *
     * @return count of enabled users
     */
    @Transactional(readOnly = true)
    public long countEnabledUsers() {
        return userRepository.countEnabledUsers();
    }

    /**
     * Validate user data before saving
     *
     * @param user the user to validate
     */
    private void validateUserData(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists: " + user.getUsername());
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }
    }

    /**
     * Update user fields from updated data
     *
     * @param existingUser the existing user
     * @param updatedUser the updated user data
     */
    private void updateUserFields(User existingUser, User updatedUser) {
        // Check if username is being changed and validate it
        if (!existingUser.getUsername().equals(updatedUser.getUsername())) {
            if (userRepository.existsByUsername(updatedUser.getUsername())) {
                throw new RuntimeException("Username already exists: " + updatedUser.getUsername());
            }
            existingUser.setUsername(updatedUser.getUsername());
        }

        // Check if email is being changed and validate it
        if (!existingUser.getEmail().equals(updatedUser.getEmail())) {
            if (userRepository.existsByEmail(updatedUser.getEmail())) {
                throw new RuntimeException("Email already exists: " + updatedUser.getEmail());
            }
            existingUser.setEmail(updatedUser.getEmail());
        }

        // Update other fields
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setEnabled(updatedUser.getEnabled());
    }
}
