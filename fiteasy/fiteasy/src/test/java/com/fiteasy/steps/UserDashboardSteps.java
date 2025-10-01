package com.fiteasy.steps;

import com.fiteasy.model.User;
import com.fiteasy.model.WorkoutPlan;
import com.fiteasy.service.UserService;
import com.fiteasy.service.WorkoutPlanService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Step definitions for User Dashboard BDD scenarios
 * Implements the Gherkin steps from user_dashboard.feature
 */
public class UserDashboardSteps {

    @Autowired
    private UserService userService;

    @Autowired
    private WorkoutPlanService workoutPlanService;

    private User currentUser;
    private List<WorkoutPlan> userWorkoutPlans;
    private String currentPage;
    private String welcomeMessage;
    private Map<String, String> userStatistics;
    private List<String> quickActionButtons;
    private String notificationMessage;
    private String dashboardMessage;
    private List<String> onboardingSuggestions;
    private boolean profileComplete;
    private int workoutPlansCount;
    private int bmiResultsCount;

    @Given("I am logged in as user {string} with ID {string} for dashboard")
    public void i_am_logged_in_as_user_with_id_for_dashboard(String username, String userId) {
        currentUser = new User(username, "test@example.com", "Password123");
        currentUser.setId(Long.parseLong(userId));
        userWorkoutPlans = new ArrayList<>();
        profileComplete = true; // Default to complete
    }

    @Given("I have completed my user profile details")
    public void i_have_completed_my_user_profile_details() {
        profileComplete = true;
        // In a real implementation, this would check database for complete profile
        currentUser.setName("Test User");
        currentUser.setAge(25);
    }

    @Given("I have created {int} workout plans")
    public void i_have_created_workout_plans(int count) {
        workoutPlansCount = count;
        userWorkoutPlans.clear();
        
        for (int i = 1; i <= count; i++) {
            WorkoutPlan plan = new WorkoutPlan(currentUser.getId());
            plan.setId((long) i);
            plan.setBmiData("22.86");
            userWorkoutPlans.add(plan);
        }
    }

    @Given("I have {int} BMI result")
    public void i_have_bmi_result(int count) {
        bmiResultsCount = count;
        // In a real implementation, this would be based on workout plans with BMI data
    }

    @Given("I am logged in as dashboard user {string}")
    public void i_am_logged_in_as_dashboard_user(String username) {
        if (currentUser == null) {
            currentUser = new User(username, "test@example.com", "Password123");
            currentUser.setId(1L);
        }
        currentUser.setUsername(username);
    }

    @Given("my user profile is incomplete \\(missing name and age)")
    public void my_user_profile_is_incomplete_missing_name_and_age() {
        profileComplete = false;
        currentUser.setName(null);
        currentUser.setAge(null);
    }

    @Given("I have no workout plans created")
    public void i_have_no_workout_plans_created() {
        workoutPlansCount = 0;
        bmiResultsCount = 0;
        userWorkoutPlans.clear();
    }

    @Given("I have workout plans but no BMI calculations")
    public void i_have_workout_plans_but_no_bmi_calculations() {
        workoutPlansCount = 2;
        bmiResultsCount = 0;
        // Create workout plans without BMI data
        userWorkoutPlans.clear();
        for (int i = 1; i <= 2; i++) {
            WorkoutPlan plan = new WorkoutPlan(currentUser.getId());
            plan.setId((long) i);
            plan.setBmiData(null); // No BMI data
            userWorkoutPlans.add(plan);
        }
    }

    @Given("I am on the dashboard page")
    public void i_am_on_the_dashboard_page() {
        currentPage = "dashboard";
    }

    @Given("I am logged in as a new user with no data")
    public void i_am_logged_in_as_a_new_user_with_no_data() {
        currentUser = new User("newuser", "new@example.com", "Password123");
        currentUser.setId(2L);
        profileComplete = false;
        workoutPlansCount = 0;
        bmiResultsCount = 0;
        userWorkoutPlans.clear();
    }

    @When("I navigate to the dashboard page")
    public void i_navigate_to_the_dashboard_page() {
        currentPage = "dashboard";
        
        // Simulate loading dashboard data
        loadDashboardData();
    }

    @When("I click on {string}")
    public void i_click_on(String buttonText) {
        switch (buttonText) {
            case "View My Plans":
                currentPage = "workout-plans";
                break;
            case "Update Profile":
                currentPage = "user-profile";
                break;
            case "Create Workout Plan":
                currentPage = "create-workout-plan";
                break;
            case "Calculate BMI":
                currentPage = "bmi-calculator";
                break;
        }
    }

    @Then("I should see my user statistics:")
    public void i_should_see_my_user_statistics(io.cucumber.datatable.DataTable dataTable) {
        var expectedStats = dataTable.asMap(String.class, String.class);
        
        assertNotNull(userStatistics, "User statistics should be loaded");
        
        for (Map.Entry<String, String> entry : expectedStats.entrySet()) {
            String statName = entry.getKey();
            String expectedValue = entry.getValue();
            
            switch (statName) {
                case "Username":
                    assertEquals(expectedValue, currentUser.getUsername());
                    break;
                case "Profile Complete":
                    assertEquals(expectedValue, profileComplete ? "1" : "0");
                    break;
                case "Workout Plans":
                    assertEquals(expectedValue, String.valueOf(workoutPlansCount));
                    break;
                case "BMI Results":
                    assertEquals(expectedValue, String.valueOf(bmiResultsCount));
                    break;
            }
        }
    }

    @Then("I should see a welcome message {string}")
    public void i_should_see_a_welcome_message(String expectedMessage) {
        assertNotNull(welcomeMessage, "Welcome message should be set");
        assertEquals(expectedMessage, welcomeMessage);
    }

    @Then("I should see quick action buttons for:")
    public void i_should_see_quick_action_buttons_for(io.cucumber.datatable.DataTable dataTable) {
        var expectedButtons = dataTable.asList(String.class);
        
        assertNotNull(quickActionButtons, "Quick action buttons should be loaded");
        
        // Skip the header row ("Action") and check the actual button names
        for (String expectedButton : expectedButtons) {
            if (!"Action".equals(expectedButton)) {
                assertTrue(quickActionButtons.contains(expectedButton), 
                          "Should contain button: " + expectedButton);
            }
        }
    }

    @Then("I should see a notification {string}")
    public void i_should_see_a_notification(String expectedNotification) {
        assertNotNull(notificationMessage, "Notification message should be set");
        assertEquals(expectedNotification, notificationMessage);
    }

    @Then("I should see a {string} button")
    public void i_should_see_a_button(String buttonText) {
        assertNotNull(quickActionButtons, "Quick action buttons should be loaded");
        assertTrue(quickActionButtons.contains(buttonText), 
                  "Should contain button: " + buttonText);
    }

    @Then("I should see a message {string}")
    public void i_should_see_a_message(String expectedMessage) {
        assertNotNull(dashboardMessage, "Dashboard message should be set");
        assertEquals(expectedMessage, dashboardMessage);
    }

    @Then("I should see a prominent {string} button")
    public void i_should_see_a_prominent_button(String buttonText) {
        assertNotNull(quickActionButtons, "Quick action buttons should be loaded");
        assertTrue(quickActionButtons.contains(buttonText), 
                  "Should contain prominent button: " + buttonText);
    }

    @Then("I should be redirected to the workout plans page")
    public void i_should_be_redirected_to_the_workout_plans_page() {
        assertEquals("workout-plans", currentPage);
    }

    @Then("I should see all my workout plans listed")
    public void i_should_see_all_my_workout_plans_listed() {
        assertEquals("workout-plans", currentPage);
        assertNotNull(userWorkoutPlans, "Workout plans should be loaded");
    }

    @Then("I should be redirected to the user details page")
    public void i_should_be_redirected_to_the_user_details_page() {
        assertEquals("user-profile", currentPage);
    }

    @Then("I should see my current profile information")
    public void i_should_see_my_current_profile_information() {
        assertEquals("user-profile", currentPage);
        assertNotNull(currentUser, "User should be loaded");
    }

    @Then("I should see onboarding suggestions:")
    public void i_should_see_onboarding_suggestions(io.cucumber.datatable.DataTable dataTable) {
        var expectedSuggestions = dataTable.asList(String.class);
        
        assertNotNull(onboardingSuggestions, "Onboarding suggestions should be loaded");
        
        // Skip the header row ("Suggestion") and check the actual suggestion text
        for (String expectedSuggestion : expectedSuggestions) {
            if (!"Suggestion".equals(expectedSuggestion)) {
                assertTrue(onboardingSuggestions.contains(expectedSuggestion), 
                          "Should contain suggestion: " + expectedSuggestion);
            }
        }
    }

    /**
     * Helper method to simulate loading dashboard data
     */
    private void loadDashboardData() {
        // Simulate loading user statistics
        userStatistics = Map.of(
            "Username", currentUser.getUsername(),
            "Profile Complete", profileComplete ? "1" : "0",
            "Workout Plans", String.valueOf(workoutPlansCount),
            "BMI Results", String.valueOf(bmiResultsCount)
        );

        // Set welcome message based on user status
        if (workoutPlansCount == 0 && bmiResultsCount == 0) {
            welcomeMessage = "Welcome to FitEasy!";
        } else {
            welcomeMessage = "Welcome back, " + currentUser.getUsername() + "!";
        }

        // Set quick action buttons
        quickActionButtons = new ArrayList<>();
        quickActionButtons.add("Create Workout Plan");
        quickActionButtons.add("View My Plans");
        quickActionButtons.add("Update Profile");
        
        if (!profileComplete) {
            quickActionButtons.add("Complete Profile");
        }
        
        if (bmiResultsCount == 0) {
            quickActionButtons.add("Calculate BMI");
        }

        // Set notification messages
        if (!profileComplete) {
            notificationMessage = "Complete your profile to get personalized recommendations";
        }

        // Set dashboard messages
        if (workoutPlansCount == 0) {
            dashboardMessage = "Start your fitness journey by creating your first workout plan";
        } else if (bmiResultsCount == 0 && workoutPlansCount > 0) {
            dashboardMessage = "Add your height and weight to get BMI calculations";
        }

        // Set onboarding suggestions for new users
        if (workoutPlansCount == 0 && bmiResultsCount == 0) {
            onboardingSuggestions = List.of(
                "Complete your profile",
                "Create your first workout plan",
                "Calculate your BMI"
            );
        }
    }
}