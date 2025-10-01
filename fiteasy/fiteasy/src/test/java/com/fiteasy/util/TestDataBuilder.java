package com.fiteasy.util;

import com.fiteasy.model.User;
import com.fiteasy.model.WorkoutPlan;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Utility class for building test data objects for BDD tests
 */
public class TestDataBuilder {

    /**
     * Create a test user with basic information
     */
    public static User createTestUser(String username, String email, String password) {
        User user = new User(username, email, password);
        user.setId(1L);
        user.setName("Test User");
        user.setAge(25);
        user.setDateCreated(LocalDate.now());
        return user;
    }

    /**
     * Create a test user with full profile information
     */
    public static User createCompleteTestUser(String username, String email, String password) {
        User user = createTestUser(username, email, password);
        user.setContacts("1234567890");
        user.setBirthDate(LocalDate.of(1998, 1, 1));
        user.setFamilyStatus("Single");
        user.setFavoriteThings("Running, Reading");
        user.setGoals("Lose weight, Build muscle");
        return user;
    }

    /**
     * Create a test workout plan with basic information
     */
    public static WorkoutPlan createTestWorkoutPlan(Long userId) {
        WorkoutPlan plan = new WorkoutPlan(userId);
        plan.setId(1L);
        plan.setAge(25);
        plan.setGender("Male");
        plan.setHeight(new BigDecimal("175.0"));
        plan.setWeight(new BigDecimal("70.0"));
        plan.setBmiData("22.86");
        plan.setTrainer("John Doe");
        plan.setGymName("FitGym");
        plan.setSpentTimeInGym("1 hour");
        plan.setWorkOut("Push-ups, Pull-ups, Squats");
        plan.setRepsSets("3 sets of 10 reps");
        plan.setDateCreated(LocalDate.now());
        return plan;
    }

    /**
     * Create a test workout plan with specific BMI data
     */
    public static WorkoutPlan createTestWorkoutPlanWithBMI(Long userId, BigDecimal height, BigDecimal weight) {
        WorkoutPlan plan = createTestWorkoutPlan(userId);
        plan.setHeight(height);
        plan.setWeight(weight);
        
        // Calculate BMI
        double heightInM = height.doubleValue() / 100;
        double bmi = weight.doubleValue() / (heightInM * heightInM);
        plan.setBmiData(String.format("%.2f", bmi));
        
        return plan;
    }

    /**
     * Create an incomplete test user (missing profile data)
     */
    public static User createIncompleteTestUser(String username, String email, String password) {
        User user = new User(username, email, password);
        user.setId(1L);
        // Missing name, age, and other profile details
        return user;
    }

    /**
     * Calculate BMI for testing purposes
     */
    public static String calculateBMI(BigDecimal height, BigDecimal weight) {
        double heightInM = height.doubleValue() / 100;
        double bmi = weight.doubleValue() / (heightInM * heightInM);
        return String.format("%.2f", bmi);
    }

    /**
     * Get BMI category for testing purposes
     */
    public static String getBMICategory(double bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 25) {
            return "Normal weight";
        } else if (bmi < 30) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    /**
     * Validate password strength
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }

    /**
     * Create a user with duplicate username scenario
     */
    public static User createDuplicateUsernameUser(String existingUsername) {
        return new User(existingUsername, "different@example.com", "Password123");
    }

    /**
     * Create a user with duplicate email scenario
     */
    public static User createDuplicateEmailUser(String existingEmail) {
        return new User("differentuser", existingEmail, "Password123");
    }
}