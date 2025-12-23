package com.enterprisesystem.controller;

import com.enterprisesystem.api.UserApi;
import com.enterprisesystem.model.User;
import com.enterprisesystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * User REST controller implementing UserApi interface
 */
@RestController
public class UserController implements UserApi {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<User> createUser(User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<User> updateUser(Long id, User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<User> getUserById(Long id) {
        try {
            User user = userService.findById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Page<User>> getAllUsers(Pageable pageable) {
        Page<User> users = userService.findAll(pageable);
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long id) {
        try {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<User> getUserByUsername(String username) {
        try {
            User user = userService.findByUsername(username);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<User> getUserByEmail(String email) {
        try {
            User user = userService.findByEmail(email);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Boolean> usernameExists(String username) {
        boolean exists = userService.usernameExists(username);
        return ResponseEntity.ok(exists);
    }

    @Override
    public ResponseEntity<Boolean> emailExists(String email) {
        boolean exists = userService.emailExists(email);
        return ResponseEntity.ok(exists);
    }

    @Override
    public ResponseEntity<User> updateUserStatus(Long id, Boolean enabled) {
        try {
            User updatedUser = userService.updateUserStatus(id, enabled);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
