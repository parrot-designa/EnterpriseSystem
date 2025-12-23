package com.enterprisesystem.repository;

import com.enterprisesystem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * User repository for data access operations
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by username
     *
     * @param username the username
     * @return optional user
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by email
     *
     * @param email the email
     * @return optional user
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if username exists
     *
     * @param username the username
     * @return true if exists
     */
    boolean existsByUsername(String username);

    /**
     * Check if email exists
     *
     * @param email the email
     * @return true if exists
     */
    boolean existsByEmail(String email);

    /**
     * Find all enabled users with pagination
     *
     * @param pageable pagination parameters
     * @return page of enabled users
     */
    @Query("SELECT u FROM User u WHERE u.enabled = true")
    Page<User> findAllEnabled(Pageable pageable);

    /**
     * Find all enabled users
     *
     * @return list of enabled users
     */
    @Query("SELECT u FROM User u WHERE u.enabled = true")
    List<User> findAllEnabled();

    /**
     * Find users by first name or last name
     *
     * @param name the name to search
     * @param pageable pagination parameters
     * @return page of users matching the name
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<User> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);

    /**
     * Count enabled users
     *
     * @return count of enabled users
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.enabled = true")
    long countEnabledUsers();
}
