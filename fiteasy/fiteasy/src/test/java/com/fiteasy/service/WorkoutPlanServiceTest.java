package com.fiteasy.service;

import com.fiteasy.model.WorkoutPlan;
import com.fiteasy.repository.WorkoutPlanRepository;
import com.fiteasy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutPlanServiceTest {

    @Mock
    private WorkoutPlanRepository workoutPlanRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WorkoutPlanService workoutPlanService;

    private WorkoutPlan testWorkoutPlan;

    @BeforeEach
    void setUp() {
        testWorkoutPlan = new WorkoutPlan(1L);
        testWorkoutPlan.setId(1L);
        testWorkoutPlan.setAge(25);
        testWorkoutPlan.setGender("Male");
        testWorkoutPlan.setHeight(new BigDecimal("175.0"));
        testWorkoutPlan.setWeight(new BigDecimal("70.0"));
        testWorkoutPlan.setTrainer("John Doe");
        testWorkoutPlan.setGymName("FitGym");
        testWorkoutPlan.setSpentTimeInGym("1 hour");
        testWorkoutPlan.setWorkOut("Push-ups, Pull-ups, Squats");
        testWorkoutPlan.setRepsSets("3 sets of 10 reps");
    }

    // RED PHASE: These tests should FAIL initially
    @Test
    void testCreateWorkoutPlan_Success() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(true);
        when(workoutPlanRepository.save(testWorkoutPlan)).thenReturn(testWorkoutPlan);

        // Act
        WorkoutPlan result = workoutPlanService.createWorkoutPlan(testWorkoutPlan);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals(25, result.getAge());
        assertEquals("Male", result.getGender());
        assertNotNull(result.getBmiData());
        verify(workoutPlanRepository, times(1)).save(testWorkoutPlan);
    }

    @Test
    void testCreateWorkoutPlan_UserNotFound() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            workoutPlanService.createWorkoutPlan(testWorkoutPlan);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
        verify(workoutPlanRepository, never()).save(any(WorkoutPlan.class));
    }

    @Test
    void testCreateWorkoutPlan_InvalidAge() {
        // Arrange
        testWorkoutPlan.setAge(-5);
        when(userRepository.existsById(1L)).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            workoutPlanService.createWorkoutPlan(testWorkoutPlan);
        });

        assertEquals("Age must be positive", exception.getMessage());
        verify(workoutPlanRepository, never()).save(any(WorkoutPlan.class));
    }

    @Test
    void testCreateWorkoutPlan_InvalidHeight() {
        // Arrange
        testWorkoutPlan.setHeight(new BigDecimal("-10.0"));
        when(userRepository.existsById(1L)).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            workoutPlanService.createWorkoutPlan(testWorkoutPlan);
        });

        assertEquals("Height must be positive", exception.getMessage());
        verify(workoutPlanRepository, never()).save(any(WorkoutPlan.class));
    }

    @Test
    void testCreateWorkoutPlan_InvalidWeight() {
        // Arrange
        testWorkoutPlan.setWeight(new BigDecimal("-5.0"));
        when(userRepository.existsById(1L)).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            workoutPlanService.createWorkoutPlan(testWorkoutPlan);
        });

        assertEquals("Weight must be positive", exception.getMessage());
        verify(workoutPlanRepository, never()).save(any(WorkoutPlan.class));
    }

    @Test
    void testCreateWorkoutPlan_EmptyWorkout() {
        // Arrange
        testWorkoutPlan.setWorkOut("");
        when(userRepository.existsById(1L)).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            workoutPlanService.createWorkoutPlan(testWorkoutPlan);
        });

        assertEquals("Workout description is required", exception.getMessage());
        verify(workoutPlanRepository, never()).save(any(WorkoutPlan.class));
    }

    @Test
    void testGetWorkoutPlansByUserId_Success() {
        // Arrange
        List<WorkoutPlan> expectedPlans = Arrays.asList(testWorkoutPlan);
        when(workoutPlanRepository.findByUserId(1L)).thenReturn(expectedPlans);

        // Act
        List<WorkoutPlan> result = workoutPlanService.getWorkoutPlansByUserId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testWorkoutPlan.getId(), result.get(0).getId());
        verify(workoutPlanRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testGetWorkoutPlanById_Success() {
        // Arrange
        when(workoutPlanRepository.findById(1L)).thenReturn(Optional.of(testWorkoutPlan));

        // Act
        Optional<WorkoutPlan> result = workoutPlanService.getWorkoutPlanById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testWorkoutPlan.getId(), result.get().getId());
        verify(workoutPlanRepository, times(1)).findById(1L);
    }

    @Test
    void testGetWorkoutPlanById_NotFound() {
        // Arrange
        when(workoutPlanRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<WorkoutPlan> result = workoutPlanService.getWorkoutPlanById(1L);

        // Assert
        assertFalse(result.isPresent());
        verify(workoutPlanRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateWorkoutPlan_Success() {
        // Arrange
        WorkoutPlan updatedPlan = new WorkoutPlan(1L);
        updatedPlan.setId(1L);
        updatedPlan.setAge(30);
        updatedPlan.setGender("Female");
        updatedPlan.setHeight(new BigDecimal("165.0"));
        updatedPlan.setWeight(new BigDecimal("60.0"));

        when(workoutPlanRepository.findById(1L)).thenReturn(Optional.of(testWorkoutPlan));
        when(workoutPlanRepository.save(any(WorkoutPlan.class))).thenReturn(updatedPlan);

        // Act
        WorkoutPlan result = workoutPlanService.updateWorkoutPlan(1L, updatedPlan);

        // Assert
        assertNotNull(result);
        assertEquals(30, result.getAge());
        assertEquals("Female", result.getGender());
        verify(workoutPlanRepository, times(1)).save(any(WorkoutPlan.class));
    }

    @Test
    void testUpdateWorkoutPlan_NotFound() {
        // Arrange
        when(workoutPlanRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            workoutPlanService.updateWorkoutPlan(1L, testWorkoutPlan);
        });

        assertEquals("Workout plan not found with id: 1", exception.getMessage());
        verify(workoutPlanRepository, never()).save(any(WorkoutPlan.class));
    }

    @Test
    void testDeleteWorkoutPlan_Success() {
        // Arrange
        when(workoutPlanRepository.findById(1L)).thenReturn(Optional.of(testWorkoutPlan));

        // Act
        workoutPlanService.deleteWorkoutPlan(1L);

        // Assert
        verify(workoutPlanRepository, times(1)).delete(testWorkoutPlan);
    }

    @Test
    void testDeleteWorkoutPlan_NotFound() {
        // Arrange
        when(workoutPlanRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            workoutPlanService.deleteWorkoutPlan(1L);
        });

        assertEquals("Workout plan not found with id: 1", exception.getMessage());
        verify(workoutPlanRepository, never()).delete(any(WorkoutPlan.class));
    }

    @Test
    void testCalculateBMI_Success() {
        // Arrange
        BigDecimal height = new BigDecimal("175.0");
        BigDecimal weight = new BigDecimal("70.0");

        // Act
        String bmi = workoutPlanService.calculateBMI(height, weight);

        // Assert
        assertNotNull(bmi);
        assertEquals("22.86", bmi);
    }

    @Test
    void testCalculateBMI_InvalidHeight() {
        // Arrange
        BigDecimal height = new BigDecimal("-175.0");
        BigDecimal weight = new BigDecimal("70.0");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            workoutPlanService.calculateBMI(height, weight);
        });

        assertEquals("Height and weight must be positive numbers", exception.getMessage());
    }

    @Test
    void testCalculateBMI_InvalidWeight() {
        // Arrange
        BigDecimal height = new BigDecimal("175.0");
        BigDecimal weight = new BigDecimal("-70.0");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            workoutPlanService.calculateBMI(height, weight);
        });

        assertEquals("Height and weight must be positive numbers", exception.getMessage());
    }

    @Test
    void testGetBMICategory_Underweight() {
        // Arrange
        BigDecimal bmi = new BigDecimal("17.5");

        // Act
        String category = workoutPlanService.getBMICategory(bmi);

        // Assert
        assertEquals("Underweight", category);
    }

    @Test
    void testGetBMICategory_NormalWeight() {
        // Arrange
        BigDecimal bmi = new BigDecimal("22.5");

        // Act
        String category = workoutPlanService.getBMICategory(bmi);

        // Assert
        assertEquals("Normal weight", category);
    }

    @Test
    void testGetBMICategory_Overweight() {
        // Arrange
        BigDecimal bmi = new BigDecimal("27.5");

        // Act
        String category = workoutPlanService.getBMICategory(bmi);

        // Assert
        assertEquals("Overweight", category);
    }

    @Test
    void testGetBMICategory_Obese() {
        // Arrange
        BigDecimal bmi = new BigDecimal("32.5");

        // Act
        String category = workoutPlanService.getBMICategory(bmi);

        // Assert
        assertEquals("Obese", category);
    }
}