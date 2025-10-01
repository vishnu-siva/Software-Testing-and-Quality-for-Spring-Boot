package com.fiteasy.service;

import com.fiteasy.model.User;
import com.fiteasy.repository.UserRepository;
import com.fiteasy.repository.WorkoutPlanRepository;
import com.fiteasy.repository.HelpingToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkoutPlanRepository workoutPlanRepository;

    @Autowired
    private HelpingToolRepository helpingToolRepository;

    // PAGE 3: SIGNUP PAGE - Register new user
    public User registerUser(User user) {
        validateUserRegistration(user);
        return userRepository.save(user);
    }

    // Comprehensive user registration validation
    private void validateUserRegistration(User user) {
        validateRequiredFields(user);
        validateUniqueConstraints(user);
        validateEmailFormat(user.getEmail());
        validatePasswordStrength(user.getPassword());
    }

    // Validate required fields
    private void validateRequiredFields(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new RuntimeException("Username is required");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email is required");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password is required");
        }
    }

    // Validate unique constraints
    private void validateUniqueConstraints(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists. Please choose a different username.");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists. Please use a different email address.");
        }
    }

    // Email format validation
    private void validateEmailFormat(String email) {
        if (!isValidEmail(email)) {
            throw new RuntimeException("Invalid email format");
        }
    }

    // Password strength validation
    private void validatePasswordStrength(String password) {
        if (!isValidPassword(password)) {
            throw new RuntimeException("Password must be at least 8 characters long");
        }
    }

    // Email validation helper method
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // More comprehensive email validation regex
        String emailRegex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";
        return email.matches(emailRegex);
    }

    // Password strength validation helper method
    private boolean isValidPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        return password.length() >= 8;
    }

    // PAGE 2: LOGIN PAGE - Authenticate user
    public Optional<User> authenticateUser(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return Optional.empty();
        }

        if (password == null || password.trim().isEmpty()) {
            return Optional.empty();
        }

        return userRepository.findByUsernameAndPassword(username, password);
    }

    // PAGE 5: USER DETAILS PAGE - Get user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // PAGE 5: USER DETAILS PAGE - Update user
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Update fields
        if (userDetails.getName() != null) {
            user.setName(userDetails.getName());
        }

        if (userDetails.getDateCreated() != null) {
            user.setDateCreated(userDetails.getDateCreated());
        }

        if (userDetails.getEmail() != null && !userDetails.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDetails.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            user.setEmail(userDetails.getEmail());
        }

        if (userDetails.getContacts() != null) {
            user.setContacts(userDetails.getContacts());
        }

        if (userDetails.getBirthDate() != null) {
            user.setBirthDate(userDetails.getBirthDate());
        }

        if (userDetails.getAge() != null) {
            user.setAge(userDetails.getAge());
        }

        if (userDetails.getFamilyStatus() != null) {
            user.setFamilyStatus(userDetails.getFamilyStatus());
        }

        if (userDetails.getFavoriteThings() != null) {
            user.setFavoriteThings(userDetails.getFavoriteThings());
        }

        if (userDetails.getGoals() != null) {
            user.setGoals(userDetails.getGoals());
        }

        return userRepository.save(user);
    }

    // PAGE 5: USER DETAILS PAGE - Delete user
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Cascade delete will handle related records
        userRepository.delete(user);
    }

    // PAGE 4: DASHBOARD - Get counts
    public Map<String, Object> getUserCounts(Long userId) {
        Map<String, Object> counts = new HashMap<>();

        // Check if user exists
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();

        // Count user details (profile completion)
        int userDetailsCount = 0;
        if (user.getName() != null && !user.getName().trim().isEmpty()) {
            userDetailsCount = 1;
        }

        // Count BMI results
        long bmiResults = workoutPlanRepository.countByUserIdAndBmiDataIsNotNull(userId);

        // Count workout plans
        long workoutPlans = workoutPlanRepository.countByUserId(userId);

        counts.put("userDetails", userDetailsCount);
        counts.put("bmiResults", (int) bmiResults);
        counts.put("workoutPlans", (int) workoutPlans);

        return counts;
    }

    // Utility methods
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Get user statistics
    public Map<String, Object> getUserStatistics(Long userId) {
        Map<String, Object> stats = new HashMap<>();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        stats.put("username", user.getUsername());
        stats.put("name", user.getName());
        stats.put("email", user.getEmail());
        stats.put("age", user.getAge());
        stats.put("memberSince", user.getCreatedAt());
        stats.put("totalWorkoutPlans", workoutPlanRepository.countByUserId(userId));
        stats.put("totalHelpingTools", helpingToolRepository.countByUserId(userId));

        return stats;
    }
}