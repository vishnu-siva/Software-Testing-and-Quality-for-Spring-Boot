package com.fiteasy.service;

import com.fiteasy.model.WorkoutPlan;
import com.fiteasy.repository.WorkoutPlanRepository;
import com.fiteasy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WorkoutPlanService {

    @Autowired
    private WorkoutPlanRepository workoutPlanRepository;

    @Autowired
    private UserRepository userRepository;

    // PAGE 6: Create workout plan with BMI calculation
    public WorkoutPlan createWorkoutPlan(WorkoutPlan workoutPlan) {
        validateWorkoutPlan(workoutPlan);
        calculateAndSetBMI(workoutPlan);
        return workoutPlanRepository.save(workoutPlan);
    }

    // Comprehensive workout plan validation
    private void validateWorkoutPlan(WorkoutPlan workoutPlan) {
        validateUserExists(workoutPlan.getUserId());
        validateWorkoutPlanFields(workoutPlan);
    }

    // Validate user exists
    private void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }

    // Validate workout plan fields
    private void validateWorkoutPlanFields(WorkoutPlan workoutPlan) {
        validateAge(workoutPlan.getAge());
        validateHeight(workoutPlan.getHeight());
        validateWeight(workoutPlan.getWeight());
        validateWorkoutDescription(workoutPlan.getWorkOut());
    }

    // Validate age
    private void validateAge(Integer age) {
        if (age != null && age <= 0) {
            throw new RuntimeException("Age must be positive");
        }
    }

    // Validate height
    private void validateHeight(BigDecimal height) {
        if (height != null && height.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Height must be positive");
        }
    }

    // Validate weight
    private void validateWeight(BigDecimal weight) {
        if (weight != null && weight.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Weight must be positive");
        }
    }

    // Validate workout description
    private void validateWorkoutDescription(String workOut) {
        if (workOut == null || workOut.trim().isEmpty()) {
            throw new RuntimeException("Workout description is required");
        }
    }

    // Calculate and set BMI if possible
    private void calculateAndSetBMI(WorkoutPlan workoutPlan) {
        if (workoutPlan.getHeight() != null && workoutPlan.getWeight() != null) {
            String bmi = calculateBMI(workoutPlan.getHeight(), workoutPlan.getWeight());
            workoutPlan.setBmiData(bmi);
        }
    }

    // PAGE 6: Get workout plans for user
    public List<WorkoutPlan> getWorkoutPlansByUserId(Long userId) {
        return workoutPlanRepository.findByUserId(userId);
    }

    // PAGE 6: Get workout plan by ID
    public Optional<WorkoutPlan> getWorkoutPlanById(Long id) {
        return workoutPlanRepository.findById(id);
    }

    // PAGE 6: Update workout plan
    public WorkoutPlan updateWorkoutPlan(Long id, WorkoutPlan planDetails) {
        WorkoutPlan plan = workoutPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Workout plan not found with id: " + id));

        // Update fields
        if (planDetails.getBmiData() != null) {
            plan.setBmiData(planDetails.getBmiData());
        }

        if (planDetails.getDateCreated() != null) {
            plan.setDateCreated(planDetails.getDateCreated());
        }

        if (planDetails.getAge() != null) {
            plan.setAge(planDetails.getAge());
        }

        if (planDetails.getGender() != null) {
            plan.setGender(planDetails.getGender());
        }

        if (planDetails.getHeight() != null) {
            plan.setHeight(planDetails.getHeight());
        }

        if (planDetails.getWeight() != null) {
            plan.setWeight(planDetails.getWeight());
        }

        // Recalculate BMI if height or weight changed
        if (plan.getHeight() != null && plan.getWeight() != null) {
            String bmi = calculateBMI(plan.getHeight(), plan.getWeight());
            plan.setBmiData(bmi);
        }

        if (planDetails.getTrainer() != null) {
            plan.setTrainer(planDetails.getTrainer());
        }

        if (planDetails.getGymName() != null) {
            plan.setGymName(planDetails.getGymName());
        }

        if (planDetails.getSpentTimeInGym() != null) {
            plan.setSpentTimeInGym(planDetails.getSpentTimeInGym());
        }

        if (planDetails.getWorkOut() != null) {
            plan.setWorkOut(planDetails.getWorkOut());
        }

        if (planDetails.getRepsSets() != null) {
            plan.setRepsSets(planDetails.getRepsSets());
        }

        return workoutPlanRepository.save(plan);
    }

    // PAGE 6: Delete workout plan
    public void deleteWorkoutPlan(Long id) {
        WorkoutPlan plan = workoutPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Workout plan not found with id: " + id));
        workoutPlanRepository.delete(plan);
    }

    // PAGE 4: Dashboard counts
    public long getWorkoutPlanCountByUserId(Long userId) {
        return workoutPlanRepository.countByUserId(userId);
    }

    public long getBMIResultsCountByUserId(Long userId) {
        return workoutPlanRepository.countByUserIdAndBmiDataIsNotNull(userId);
    }

    // PAGE 6: BMI CALCULATION - Mathematical Formula
    public String calculateBMI(BigDecimal height, BigDecimal weight) {
        if (height == null || weight == null ||
                height.compareTo(BigDecimal.ZERO) <= 0 ||
                weight.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Height and weight must be positive numbers");
        }

        // Convert height from cm to meters
        BigDecimal heightInMeters = height.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);

        // Calculate BMI = weight / (height^2)
        BigDecimal heightSquared = heightInMeters.multiply(heightInMeters);
        BigDecimal bmi = weight.divide(heightSquared, 2, RoundingMode.HALF_UP);

        return bmi.toString();
    }

    // PAGE 6: Get BMI category
    public String getBMICategory(BigDecimal bmi) {
        if (bmi == null) {
            return "Unknown";
        }

        if (bmi.compareTo(new BigDecimal("18.5")) < 0) {
            return "Underweight";
        } else if (bmi.compareTo(new BigDecimal("25")) < 0) {
            return "Normal weight";
        } else if (bmi.compareTo(new BigDecimal("30")) < 0) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    // Get latest workout plan for user
    public Optional<WorkoutPlan> getLatestWorkoutPlanByUserId(Long userId) {
        List<WorkoutPlan> plans = workoutPlanRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return plans.isEmpty() ? Optional.empty() : Optional.of(plans.get(0));
    }

    // Get workout plans with BMI data
    public List<WorkoutPlan> getWorkoutPlansWithBMI(Long userId) {
        return workoutPlanRepository.findByUserIdAndBmiDataIsNotNull(userId);
    }

    // Delete all workout plans for user (cascade delete)
    public void deleteWorkoutPlansByUserId(Long userId) {
        workoutPlanRepository.deleteByUserId(userId);
    }

    // Admin functions
    public List<WorkoutPlan> getAllWorkoutPlans() {
        return workoutPlanRepository.findAll();
    }

    public List<WorkoutPlan> getWorkoutPlansByTrainer(String trainerName) {
        return workoutPlanRepository.findByTrainerContainingIgnoreCase(trainerName);
    }

    public List<WorkoutPlan> getWorkoutPlansByGym(String gymName) {
        return workoutPlanRepository.findByGymNameContainingIgnoreCase(gymName);
    }
}