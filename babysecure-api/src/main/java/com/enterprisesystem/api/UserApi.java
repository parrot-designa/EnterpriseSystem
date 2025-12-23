package com.enterprisesystem.api;

import com.enterprisesystem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * User API interface defining all CRUD operations
 */
@Validated
@RequestMapping("/api/users")
public interface UserApi {

    /**
     * Create a new user
     *
     * @param user the user to create
     * @return the created user
     */
    @PostMapping
    ResponseEntity<User> createUser(@Valid @RequestBody User user);

    /**
     * Update an existing user
     *
     * @param id the user ID
     * @param user the updated user data
     * @return the updated user
     */
    @PutMapping("/{id}")
    ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user);

    /**
     * Get user by ID
     *
     * @param id the user ID
     * @return the user if found
     */
    @GetMapping("/{id}")
    ResponseEntity<User> getUserById(@PathVariable Long id);

    /**
     * Get all users with pagination
     *
     * @param pageable pagination parameters
     * @return page of users
     */
    @GetMapping
    ResponseEntity<Page<User>> getAllUsers(Pageable pageable);

    /**
     * Get all users without pagination
     *
     * @return list of all users
     */
    @GetMapping("/list")
    ResponseEntity<List<User>> getAllUsers();

    /**
     * Delete user by ID
     *
     * @param id the user ID
     * @return success response
     */
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable Long id);

    /**
     * Find user by username
     *
     * @param username the username
     * @return the user if found
     */
    @GetMapping("/username/{username}")
    ResponseEntity<User> getUserByUsername(@PathVariable String username);

    /**
     * Find user by email
     *
     * @param email the email
     * @return the user if found
     */
    @GetMapping("/email/{email}")
    ResponseEntity<User> getUserByEmail(@PathVariable String email);

    /**
     * Check if username exists
     *
     * @param username the username
     * @return true if exists
     */
    @GetMapping("/exists/username/{username}")
    ResponseEntity<Boolean> usernameExists(@PathVariable String username);

    /**
     * Check if email exists
     *
     * @param email the email
     * @return true if exists
     */
    @GetMapping("/exists/email/{email}")
    ResponseEntity<Boolean> emailExists(@PathVariable String email);

    /**
     * Enable or disable user
     *
     * @param id the user ID
     * @param enabled the enabled status
     * @return the updated user
     */
    @PatchMapping("/{id}/status")
    ResponseEntity<User> updateUserStatus(@PathVariable Long id, @RequestParam Boolean enabled);
}