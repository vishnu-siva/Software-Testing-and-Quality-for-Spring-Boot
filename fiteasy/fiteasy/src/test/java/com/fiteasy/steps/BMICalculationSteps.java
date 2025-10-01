package com.fiteasy.steps;

import com.fiteasy.model.User;
import com.fiteasy.model.WorkoutPlan;
import com.fiteasy.service.WorkoutPlanService;
import com.fiteasy.service.HelpingToolService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Step definitions for BMI Calculation BDD scenarios
 * Implements the Gherkin steps from bmi_calculation.feature
 */
public class BMICalculationSteps {

    @Autowired
    private WorkoutPlanService workoutPlanService;

    @Autowired
    private HelpingToolService helpingToolService;

    private WorkoutPlan currentWorkoutPlan;
    private User currentUser;
    private Exception caughtException;
    private String calculatedBMI;
    private String bmiCategory;
    private String displayMessage;

    @Given("I am logged in as a registered user")
    public void i_am_logged_in_as_a_registered_user() {
        // Simulate logged-in user
        currentUser = new User("testuser", "test@example.com", "Password123");
        currentUser.setId(1L);
        currentWorkoutPlan = new WorkoutPlan(1L);
    }


    @Given("I am creating a workout plan")
    public void i_am_creating_a_workout_plan() {
        assertNotNull(currentWorkoutPlan, "Workout plan should be initialized");
        assertNotNull(currentUser, "User should be logged in");
    }

    @When("I enter my physical measurements:")
    public void i_enter_my_physical_measurements(io.cucumber.datatable.DataTable dataTable) {
        var data = dataTable.asMap(String.class, String.class);
        
        if (data.containsKey("Height (cm)")) {
            currentWorkoutPlan.setHeight(new BigDecimal(data.get("Height (cm)")));
        }
        if (data.containsKey("Weight (kg)")) {
            currentWorkoutPlan.setWeight(new BigDecimal(data.get("Weight (kg)")));
        }
    }

    @When("I submit the workout plan")
    public void i_submit_the_workout_plan() {
        try {
            // Calculate BMI using the helping tool service
            BigDecimal height = currentWorkoutPlan.getHeight();
            BigDecimal weight = currentWorkoutPlan.getWeight();
            
            if (height != null && weight != null && 
                height.compareTo(BigDecimal.ZERO) > 0 && 
                weight.compareTo(BigDecimal.ZERO) > 0) {
                
                calculatedBMI = helpingToolService.calculateBMI(height, weight);
                bmiCategory = helpingToolService.getBMICategory(new BigDecimal(calculatedBMI));
                displayMessage = helpingToolService.getBMIMessage(bmiCategory);
                
                currentWorkoutPlan.setBmiData(calculatedBMI);
            }
        } catch (Exception e) {
            caughtException = e;
        }
    }

    @When("I try to submit the workout plan")
    public void i_try_to_submit_the_workout_plan() {
        try {
            BigDecimal height = currentWorkoutPlan.getHeight();
            BigDecimal weight = currentWorkoutPlan.getWeight();
            
            // Validation checks - check for zero values first
            if ((height != null && height.compareTo(BigDecimal.ZERO) == 0) ||
                (weight != null && weight.compareTo(BigDecimal.ZERO) == 0)) {
                throw new IllegalArgumentException("Height and weight must be positive numbers");
            }
            
            // Then check for negative values or nulls
            if (height == null || height.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Height must be positive");
            }
            if (weight == null || weight.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Weight must be positive");
            }
            
            calculatedBMI = helpingToolService.calculateBMI(height, weight);
            bmiCategory = helpingToolService.getBMICategory(new BigDecimal(calculatedBMI));
        } catch (Exception e) {
            caughtException = e;
        }
    }

    @Then("my BMI should be calculated as {string}")
    public void my_bmi_should_be_calculated_as(String expectedBMI) {
        assertNull(caughtException, "No exception should be thrown during BMI calculation");
        assertNotNull(calculatedBMI, "BMI should be calculated");
        assertEquals(expectedBMI, calculatedBMI);
    }

    @Then("my BMI category should be {string}")
    public void my_bmi_category_should_be(String expectedCategory) {
        assertNotNull(bmiCategory, "BMI category should be determined");
        assertEquals(expectedCategory, bmiCategory);
    }

    @Then("I should see a BMI message {string}")
    public void i_should_see_a_bmi_message(String expectedMessage) {
        assertNotNull(displayMessage, "Display message should be set");
        assertEquals(expectedMessage, displayMessage);
    }

    @Then("I should see a BMI error message {string}")
    public void i_should_see_a_bmi_error_message(String expectedMessage) {
        assertNotNull(caughtException, "An exception should be thrown");
        assertEquals(expectedMessage, caughtException.getMessage());
    }

    @Then("no BMI calculation should be performed")
    public void no_bmi_calculation_should_be_performed() {
        assertNotNull(caughtException, "An exception should prevent BMI calculation");
        assertNull(calculatedBMI, "BMI should not be calculated");
    }

    @Then("the BMI should be rounded to {int} decimal places")
    public void the_bmi_should_be_rounded_to_decimal_places(int decimalPlaces) {
        assertNotNull(calculatedBMI, "BMI should be calculated");
        
        // Check if the BMI has the correct number of decimal places
        String[] parts = calculatedBMI.split("\\.");
        if (parts.length > 1) {
            assertTrue(parts[1].length() <= decimalPlaces, 
                      "BMI should be rounded to " + decimalPlaces + " decimal places");
        }
    }

    // Helper method to get BMI category based on BMI value
    private String getBMICategory(BigDecimal bmi) {
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

    // Helper method to get appropriate message based on BMI category
    private String getBMIMessage(String category) {
        switch (category) {
            case "Underweight":
                return "Your BMI indicates you may be underweight. Consider consulting a healthcare provider.";
            case "Normal weight":
                return "Your BMI indicates a healthy weight range";
            case "Overweight":
                return "Your BMI indicates you are overweight. Consider a balanced diet and regular exercise.";
            case "Obese":
                return "Your BMI indicates obesity. Please consult a healthcare provider for guidance.";
            default:
                return "BMI calculated successfully";
        }
    }
}