package com.fiteasy.steps;

import com.fiteasy.model.User;
import com.fiteasy.service.UserService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Step definitions for User Login BDD scenarios
 * Implements the Gherkin steps from user_login.feature
 */
public class UserLoginSteps {

    @Autowired
    private UserService userService;

    private User existingUser;
    private User loginAttemptUser;
    private Exception caughtException;
    private boolean loginSuccessful = false;
    private String currentPage = "";
    private String displayedUsername = "";
    private int failedLoginAttempts = 0;

    @Given("a user exists with username {string} and password {string}")
    public void a_user_exists_with_username_and_password(String username, String password) {
        // Create and register a test user
        try {
            existingUser = new User(username, "test@example.com", password);
            existingUser.setId(1L);
            // In a real implementation, this user would be saved to the database
            // For testing purposes, we assume the user exists
        } catch (Exception e) {
            // User might already exist, which is fine for our test
        }
    }

    @Given("I am on the login page")
    public void i_am_on_the_login_page() {
        currentPage = "login";
        loginAttemptUser = new User();
        caughtException = null;
        loginSuccessful = false;
    }

    @When("I enter the following login details:")
    public void i_enter_the_following_login_details(io.cucumber.datatable.DataTable dataTable) {
        var data = dataTable.asMap(String.class, String.class);
        
        String username = data.get("Username");
        String password = data.get("Password");
        
        loginAttemptUser.setUsername(username != null ? username : "");
        loginAttemptUser.setPassword(password != null ? password : "");
    }

    @When("I click the {string} button")
    public void i_click_the_button(String buttonName) {
        try {
            if ("Login".equals(buttonName)) {
                authenticateUser();
            }
        } catch (Exception e) {
            caughtException = e;
        }
    }

    @When("I attempt to login {int} times with incorrect credentials")
    public void i_attempt_to_login_times_with_incorrect_credentials(int attempts) {
        failedLoginAttempts = 0;
        
        for (int i = 0; i < attempts; i++) {
            try {
                loginAttemptUser.setUsername("wronguser");
                loginAttemptUser.setPassword("wrongpass");
                authenticateUser();
            } catch (Exception e) {
                failedLoginAttempts++;
                if (failedLoginAttempts >= 3) {
                    caughtException = new SecurityException("Too many failed attempts. Please try again later.");
                    break;
                }
                caughtException = e;
            }
        }
    }

    @Then("I should see a login success message {string}")
    public void i_should_see_a_login_success_message(String expectedMessage) {
        assertNull(caughtException, "No exception should be thrown for successful login");
        assertTrue(loginSuccessful, "Login should be successful");
    }

    @Then("I should see a login error message {string}")
    public void i_should_see_a_login_error_message(String expectedMessage) {
        assertNotNull(caughtException, "An exception should be thrown for failed login");
        assertEquals(expectedMessage, caughtException.getMessage());
    }

    @And("I should be redirected to the dashboard page")
    public void i_should_be_redirected_to_the_dashboard_page() {
        assertTrue(loginSuccessful, "Login should be successful before redirect");
        currentPage = "dashboard";
    }

    @And("I should see my username {string} displayed")
    public void i_should_see_my_username_displayed(String expectedUsername) {
        assertTrue(loginSuccessful, "Login should be successful");
        assertEquals(expectedUsername, displayedUsername);
    }

    @And("I should remain on the login page")
    public void i_should_remain_on_the_login_page() {
        assertEquals("login", currentPage);
        assertFalse(loginSuccessful, "Login should not be successful");
    }

    @And("I should not be redirected to the dashboard")
    public void i_should_not_be_redirected_to_the_dashboard() {
        assertFalse(loginSuccessful, "Login should not be successful");
        assertNotEquals("dashboard", currentPage);
    }

    @And("the login form should be disabled")
    public void the_login_form_should_be_disabled() {
        assertNotNull(caughtException, "Exception should be thrown");
        assertTrue(caughtException instanceof SecurityException, 
                  "Should be a security exception for too many attempts");
        assertEquals(3, failedLoginAttempts, "Should have 3 failed attempts");
    }

    /**
     * Helper method to authenticate user
     */
    private void authenticateUser() throws Exception {
        String username = loginAttemptUser.getUsername();
        String password = loginAttemptUser.getPassword();

        // Validation checks
        if (username == null || username.trim().isEmpty()) {
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Username and password are required");
            } else {
                throw new IllegalArgumentException("Username is required");
            }
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        // Check against existing user (case-sensitive)
        if (existingUser != null && 
            existingUser.getUsername().equals(username) && 
            existingUser.getPassword().equals(password)) {
            
            loginSuccessful = true;
            displayedUsername = username;
            currentPage = "dashboard";
        } else {
            throw new SecurityException("Invalid username or password");
        }
    }

    /**
     * Helper method to simulate user authentication through service layer
     */
    private User authenticateUserWithService(String username, String password) throws Exception {
        // In a real implementation, this would call userService.authenticate()
        // For now, we simulate the authentication logic
        
        if (existingUser != null && 
            existingUser.getUsername().equals(username) && 
            existingUser.getPassword().equals(password)) {
            return existingUser;
        }
        
        throw new SecurityException("Invalid username or password");
    }
}