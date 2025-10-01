package com.fiteasy.repository;

import com.fiteasy.model.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {

    // Workout plan functionality
    List<WorkoutPlan> findByUserId(Long userId);
    List<WorkoutPlan> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Dashboard counts
    long countByUserId(Long userId);
    long countByUserIdAndBmiDataIsNotNull(Long userId);

    // BMI related queries
    List<WorkoutPlan> findByUserIdAndBmiDataIsNotNull(Long userId);

    // Search functionality
    List<WorkoutPlan> findByTrainerContainingIgnoreCase(String trainer);
    List<WorkoutPlan> findByGymNameContainingIgnoreCase(String gymName);
    List<WorkoutPlan> findByGender(String gender);

    // Delete operations
    @Modifying
    @Transactional
    @Query("DELETE FROM WorkoutPlan w WHERE w.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    // Custom queries
    @Query("SELECT w FROM WorkoutPlan w WHERE w.userId = :userId AND w.bmiData IS NOT NULL ORDER BY w.createdAt DESC")
    List<WorkoutPlan> findLatestBMIResults(@Param("userId") Long userId);

    @Query("SELECT w FROM WorkoutPlan w WHERE w.userId = :userId AND w.age BETWEEN :minAge AND :maxAge")
    List<WorkoutPlan> findByUserIdAndAgeRange(@Param("userId") Long userId, @Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);
}