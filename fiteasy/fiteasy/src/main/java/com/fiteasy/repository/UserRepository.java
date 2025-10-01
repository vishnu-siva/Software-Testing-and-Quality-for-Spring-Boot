package com.fiteasy.repository;

import com.fiteasy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Login functionality
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameAndPassword(String username, String password);

    // Signup validation
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // Search functionality
    List<User> findByNameContainingIgnoreCase(String name);
    List<User> findByUsernameContainingIgnoreCase(String username);

    // Custom queries
    @Query("SELECT u FROM User u WHERE u.age BETWEEN :minAge AND :maxAge")
    List<User> findUsersByAgeRange(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);

    @Query("SELECT COUNT(u) FROM User u WHERE u.name IS NOT NULL AND u.name != ''")
    long countUsersWithProfiles();
}