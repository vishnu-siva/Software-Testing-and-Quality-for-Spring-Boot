package com.fiteasy.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "workout_plans")
public class WorkoutPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "bmi_data")
    private String bmiData;

    @Column(name = "date_created")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateCreated;

    private Integer age;

    private String gender;

    @Column(precision = 5, scale = 2)
    private BigDecimal height;

    @Column(precision = 5, scale = 2)
    private BigDecimal weight;

    private String trainer;

    @Column(name = "gym_name")
    private String gymName;

    @Column(name = "spent_time_in_gym")
    private String spentTimeInGym;

    @Column(name = "work_out", length = 500)
    private String workOut;

    @Column(name = "reps_sets")
    private String repsSets;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructors
    public WorkoutPlan() {
        this.createdAt = LocalDateTime.now();
    }

    public WorkoutPlan(Long userId) {
        this();
        this.userId = userId;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getBmiData() { return bmiData; }
    public void setBmiData(String bmiData) { this.bmiData = bmiData; }

    public LocalDate getDateCreated() { return dateCreated; }
    public void setDateCreated(LocalDate dateCreated) { this.dateCreated = dateCreated; }

    // Alias for setDateCreated - used in BDD tests
    public void setCreatedDate(LocalDate createdDate) { this.dateCreated = createdDate; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public BigDecimal getHeight() { return height; }
    public void setHeight(BigDecimal height) { this.height = height; }

    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }

    public String getTrainer() { return trainer; }
    public void setTrainer(String trainer) { this.trainer = trainer; }

    public String getGymName() { return gymName; }
    public void setGymName(String gymName) { this.gymName = gymName; }

    public String getSpentTimeInGym() { return spentTimeInGym; }
    public void setSpentTimeInGym(String spentTimeInGym) { this.spentTimeInGym = spentTimeInGym; }

    public String getWorkOut() { return workOut; }
    public void setWorkOut(String workOut) { this.workOut = workOut; }

    public String getRepsSets() { return repsSets; }
    public void setRepsSets(String repsSets) { this.repsSets = repsSets; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "WorkoutPlan{" +
                "id=" + id +
                ", userId=" + userId +
                ", bmiData='" + bmiData + '\'' +
                ", dateCreated=" + dateCreated +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                '}';
    }
}