package com.fiteasy.controller;

import com.fiteasy.model.WorkoutPlan;
import com.fiteasy.service.WorkoutPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/workout-plans")
@CrossOrigin(origins = "http://localhost:3000")
public class WorkoutController {

    @Autowired
    private WorkoutPlanService workoutPlanService;

    //  Create new workout plan with BMI calculation
    @PostMapping
    public ResponseEntity<?> createWorkoutPlan(@RequestBody WorkoutPlan workoutPlan) {
        try {
            // Calculate BMI if height and weight provided
            if (workoutPlan.getHeight() != null && workoutPlan.getWeight() != null) {
                String bmi = workoutPlanService.calculateBMI(workoutPlan.getHeight(), workoutPlan.getWeight());
                workoutPlan.setBmiData(bmi);
            }

            WorkoutPlan createdPlan = workoutPlanService.createWorkoutPlan(workoutPlan);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Workout plan created successfully");
            response.put("workoutPlan", createdPlan);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error creating workout plan: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    //  Get all workout plans for user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WorkoutPlan>> getWorkoutPlansByUserId(@PathVariable Long userId) {
        List<WorkoutPlan> plans = workoutPlanService.getWorkoutPlansByUserId(userId);
        return ResponseEntity.ok(plans);
    }

    // Get specific workout plan by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getWorkoutPlanById(@PathVariable Long id) {
        try {
            Optional<WorkoutPlan> plan = workoutPlanService.getWorkoutPlanById(id);
            if (plan.isPresent()) {
                return ResponseEntity.ok(plan.get());
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Workout plan not found");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error fetching workout plan: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    //  Update workout plan with BMI recalculation
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWorkoutPlan(@PathVariable Long id, @RequestBody WorkoutPlan planDetails) {
        try {
            // Recalculate BMI if height and weight provided
            if (planDetails.getHeight() != null && planDetails.getWeight() != null) {
                String bmi = workoutPlanService.calculateBMI(planDetails.getHeight(), planDetails.getWeight());
                planDetails.setBmiData(bmi);
            }

            WorkoutPlan updatedPlan = workoutPlanService.updateWorkoutPlan(id, planDetails);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Workout plan updated successfully");
            response.put("workoutPlan", updatedPlan);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    //  Delete workout plan
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkoutPlan(@PathVariable Long id) {
        try {
            workoutPlanService.deleteWorkoutPlan(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Workout plan deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    //  BMI CALCULATOR - Calculate BMI endpoint
    @PostMapping("/calculate-bmi")
    public ResponseEntity<?> calculateBMIEndpoint(@RequestBody Map<String, Object> request) {
        try {
            // Validate input
            if (request.get("height") == null || request.get("weight") == null) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Both height and weight are required");
                return ResponseEntity.badRequest().body(error);
            }

            BigDecimal height = new BigDecimal(request.get("height").toString());
            BigDecimal weight = new BigDecimal(request.get("weight").toString());

            // Validate positive values
            if (height.compareTo(BigDecimal.ZERO) <= 0 || weight.compareTo(BigDecimal.ZERO) <= 0) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Height and weight must be positive numbers");
                return ResponseEntity.badRequest().body(error);
            }

            String bmi = workoutPlanService.calculateBMI(height, weight);
            String category = workoutPlanService.getBMICategory(new BigDecimal(bmi));

            Map<String, Object> response = new HashMap<>();
            response.put("bmi", bmi);
            response.put("category", category);
            response.put("message", "BMI calculated successfully");
            response.put("formula", "BMI = weight(kg) / height(m)²");

            // Add health recommendations based on BMI category
            Map<String, String> recommendations = new HashMap<>();
            switch (category) {
                case "Underweight":
                    recommendations.put("advice", "Consider gaining weight through healthy nutrition and strength training");
                    break;
                case "Normal weight":
                    recommendations.put("advice", "Maintain your current weight through balanced diet and regular exercise");
                    break;
                case "Overweight":
                    recommendations.put("advice", "Consider weight loss through cardio exercises and balanced nutrition");
                    break;
                case "Obese":
                    recommendations.put("advice", "Consult a healthcare provider for a comprehensive weight management plan");
                    break;
                default:
                    recommendations.put("advice", "Please consult a healthcare provider for personalized advice");
                    break;
            }
            response.put("recommendations", recommendations);

            return ResponseEntity.ok(response);
        } catch (NumberFormatException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Invalid number format for height or weight");
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error calculating BMI: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    //  Get workout plan count for dashboard
    @GetMapping("/count/user/{userId}")
    public ResponseEntity<Map<String, Long>> getWorkoutPlanCount(@PathVariable Long userId) {
        try {
            long count = workoutPlanService.getWorkoutPlanCountByUserId(userId);
            Map<String, Long> response = new HashMap<>();
            response.put("count", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Long> response = new HashMap<>();
            response.put("count", 0L);
            return ResponseEntity.ok(response);
        }
    }

    //  Get BMI results count for dashboard
    @GetMapping("/bmi-count/user/{userId}")
    public ResponseEntity<Map<String, Long>> getBMIResultsCount(@PathVariable Long userId) {
        try {
            long count = workoutPlanService.getBMIResultsCountByUserId(userId);
            Map<String, Long> response = new HashMap<>();
            response.put("bmiResultsCount", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Long> response = new HashMap<>();
            response.put("bmiResultsCount", 0L);
            return ResponseEntity.ok(response);
        }
    }

    // Get latest workout plan for user
    @GetMapping("/latest/user/{userId}")
    public ResponseEntity<?> getLatestWorkoutPlan(@PathVariable Long userId) {
        try {
            Optional<WorkoutPlan> latestPlan = workoutPlanService.getLatestWorkoutPlanByUserId(userId);
            if (latestPlan.isPresent()) {
                return ResponseEntity.ok(latestPlan.get());
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "No workout plans found for this user");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error fetching latest workout plan: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // PAGE 6: Get workout plans with BMI data only
    @GetMapping("/with-bmi/user/{userId}")
    public ResponseEntity<List<WorkoutPlan>> getWorkoutPlansWithBMI(@PathVariable Long userId) {
        try {
            List<WorkoutPlan> plans = workoutPlanService.getWorkoutPlansWithBMI(userId);
            return ResponseEntity.ok(plans);
        } catch (Exception e) {
            return ResponseEntity.ok(List.of()); // Return empty list on error
        }
    }

    // Admin endpoints
    @GetMapping("/all")
    public ResponseEntity<List<WorkoutPlan>> getAllWorkoutPlans() {
        try {
            List<WorkoutPlan> plans = workoutPlanService.getAllWorkoutPlans();
            return ResponseEntity.ok(plans);
        } catch (Exception e) {
            return ResponseEntity.ok(List.of()); // Return empty list on error
        }
    }

    @GetMapping("/search/trainer/{trainerName}")
    public ResponseEntity<List<WorkoutPlan>> getWorkoutPlansByTrainer(@PathVariable String trainerName) {
        try {
            List<WorkoutPlan> plans = workoutPlanService.getWorkoutPlansByTrainer(trainerName);
            return ResponseEntity.ok(plans);
        } catch (Exception e) {
            return ResponseEntity.ok(List.of()); // Return empty list on error
        }
    }

    @GetMapping("/search/gym/{gymName}")
    public ResponseEntity<List<WorkoutPlan>> getWorkoutPlansByGym(@PathVariable String gymName) {
        try {
            List<WorkoutPlan> plans = workoutPlanService.getWorkoutPlansByGym(gymName);
            return ResponseEntity.ok(plans);
        } catch (Exception e) {
            return ResponseEntity.ok(List.of()); // Return empty list on error
        }
    }
}