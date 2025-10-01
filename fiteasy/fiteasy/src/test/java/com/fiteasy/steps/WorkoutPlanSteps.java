package com.fiteasy.steps;

import com.fiteasy.model.WorkoutPlan;
import com.fiteasy.service.WorkoutPlanService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Step definitions for Workout Plan BDD scenarios
 * Implements the Gherkin steps from workout_plan_creation.feature
 */
public class WorkoutPlanSteps {

    @Autowired
    private WorkoutPlanService workoutPlanService;

    private WorkoutPlan testWorkoutPlan;
    private Exception caughtException;
    private WorkoutPlan createdWorkoutPlan;

    @Given("I am logged in as a registered user with ID {string}")
    public void i_am_logged_in_as_a_registered_user_with_id(String userId) {
        // Simulate user login - in real implementation, this would be handled by authentication
        testWorkoutPlan = new WorkoutPlan(Long.parseLong(userId));
    }

    @Given("I am on the workout plan creation page")
    public void i_am_on_the_workout_plan_creation_page() {
        // Simulate being on workout plan creation page
        assertNotNull(testWorkoutPlan, "User should be logged in");
    }

    @When("I enter the following workout plan details:")
    public void i_enter_the_following_workout_plan_details(io.cucumber.datatable.DataTable dataTable) {
        var data = dataTable.asMap(String.class, String.class);
        
        if (data.containsKey("Age")) {
            testWorkoutPlan.setAge(Integer.parseInt(data.get("Age")));
        }
        if (data.containsKey("Gender")) {
            testWorkoutPlan.setGender(data.get("Gender"));
        }
        if (data.containsKey("Height (cm)")) {
            testWorkoutPlan.setHeight(new BigDecimal(data.get("Height (cm)")));
        }
        if (data.containsKey("Weight (kg)")) {
            testWorkoutPlan.setWeight(new BigDecimal(data.get("Weight (kg)")));
        }
        if (data.containsKey("Trainer")) {
            testWorkoutPlan.setTrainer(data.get("Trainer"));
        }
        if (data.containsKey("Gym Name")) {
            testWorkoutPlan.setGymName(data.get("Gym Name"));
        }
        if (data.containsKey("Time in Gym")) {
            testWorkoutPlan.setSpentTimeInGym(data.get("Time in Gym"));
        }
        if (data.containsKey("Workout")) {
            testWorkoutPlan.setWorkOut(data.get("Workout"));
        }
        if (data.containsKey("Reps/Sets")) {
            testWorkoutPlan.setRepsSets(data.get("Reps/Sets"));
        }
    }

    @When("I press the {string} button on the workout plan page")
    public void i_press_the_button_on_the_workout_plan_page(String buttonName) {
        try {
            if ("Create Workout Plan".equals(buttonName)) {
                createdWorkoutPlan = workoutPlanService.createWorkoutPlan(testWorkoutPlan);
            }
        } catch (Exception e) {
            caughtException = e;
        }
    }

    @Then("I should see a workout creation success message {string}")
    public void i_should_see_a_workout_creation_success_message(String expectedMessage) {
        assertNull(caughtException, "No exception should be thrown");
        assertNotNull(createdWorkoutPlan, "Workout plan should be created successfully");
    }

    @Then("I should see a workout creation error message {string}")
    public void i_should_see_a_workout_creation_error_message(String expectedMessage) {
        assertNotNull(caughtException, "An exception should be thrown");
        assertEquals(expectedMessage, caughtException.getMessage());
    }

    @And("the workout plan should be saved with BMI data {string}")
    public void the_workout_plan_should_be_saved_with_bmi_data(String expectedBmi) {
        assertNotNull(createdWorkoutPlan, "Workout plan should be created");
        assertEquals(expectedBmi, createdWorkoutPlan.getBmiData());
    }

    @And("I should be redirected to my workout plans page")
    public void i_should_be_redirected_to_my_workout_plans_page() {
        assertNotNull(createdWorkoutPlan, "Workout plan should be created before redirect");
    }

    @And("I should remain on the workout plan creation page")
    public void i_should_remain_on_the_workout_plan_creation_page() {
        assertNotNull(caughtException, "Should remain on page due to error");
    }

    @And("no workout plan should be created")
    public void no_workout_plan_should_be_created() {
        assertNull(createdWorkoutPlan, "No workout plan should be created");
    }

    @Given("I am not logged in or user with ID {string} does not exist")
    public void i_am_not_logged_in_or_user_with_id_does_not_exist(String userId) {
        // Simulate non-existent user
        testWorkoutPlan = new WorkoutPlan(Long.parseLong(userId));
    }

    @When("I try to create a workout plan with user ID {string}")
    public void i_try_to_create_a_workout_plan_with_user_id(String userId) {
        try {
            createdWorkoutPlan = workoutPlanService.createWorkoutPlan(testWorkoutPlan);
        } catch (Exception e) {
            caughtException = e;
        }
    }

    // Additional step definitions for workout plan creation scenarios

    @Given("I am logged in as user with ID {string} for workout creation")
    public void i_am_logged_in_as_user_with_id_for_workout_creation(String userId) {
        // Simulate user login - in real implementation, this would be handled by authentication
        testWorkoutPlan = new WorkoutPlan(Long.parseLong(userId));
    }

    @When("I click the workout {string} button")
    public void i_click_the_workout_button(String buttonName) {
        try {
            if ("Create Workout Plan".equals(buttonName)) {
                createdWorkoutPlan = workoutPlanService.createWorkoutPlan(testWorkoutPlan);
            }
        } catch (Exception e) {
            caughtException = e;
        }
    }

    @And("the BMI category should be {string}")
    public void the_bmi_category_should_be(String expectedCategory) {
        assertNotNull(createdWorkoutPlan, "Workout plan should be created");
        String bmiData = createdWorkoutPlan.getBmiData();
        assertNotNull(bmiData, "BMI data should be available");
        
        String actualCategory = getBMICategory(Double.parseDouble(bmiData));
        assertEquals(expectedCategory, actualCategory);
    }


    /**
     * Helper method to get BMI category based on BMI value
     */
    private String getBMICategory(double bmi) {
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
}
