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
 * Step definitions for User Registration BDD scenarios
 * Implements the Gherkin steps from user_registration.feature
 */
public class UserRegistrationSteps {

    @Autowired
    private UserService userService;

    private User testUser;
    private Exception caughtException;
    private User registeredUser;

    @Given("the FitEasy application is running")
    public void the_fiteasy_application_is_running() {
        // Application context is loaded by Spring Boot Test
        assertNotNull(userService, "UserService should be available");
    }

    @Given("no user exists with the email {string}")
    public void no_user_exists_with_the_email(String email) {
        // This would typically involve database setup
        // For now, we assume clean state
    }

    @Given("I am on the registration page")
    public void i_am_on_the_registration_page() {
        // Simulate being on registration page
        testUser = new User();
    }

    @When("I enter the following registration details:")
    public void i_enter_the_following_registration_details(io.cucumber.datatable.DataTable dataTable) {
        var data = dataTable.asMap(String.class, String.class);
        testUser = new User(
            data.get("Username"),
            data.get("Email"),
            data.get("Password")
        );
    }

    @When("I click the registration {string} button")
    public void i_click_the_registration_button(String buttonName) {
        try {
            if ("Register".equals(buttonName)) {
                registeredUser = userService.registerUser(testUser);
            }
        } catch (Exception e) {
            caughtException = e;
        }
    }

    @Then("I should see a registration success message {string}")
    public void i_should_see_a_registration_success_message(String expectedMessage) {
        assertNull(caughtException, "No exception should be thrown");
        assertNotNull(registeredUser, "User should be registered successfully");
    }

    @Then("I should see a registration error message {string}")
    public void i_should_see_a_registration_error_message(String expectedMessage) {
        assertNotNull(caughtException, "An exception should be thrown");
        assertEquals(expectedMessage, caughtException.getMessage());
    }

    @And("I should be redirected to the login page")
    public void i_should_be_redirected_to_the_login_page() {
        // Simulate redirect - in real implementation, this would be handled by controller
        assertNotNull(registeredUser, "User should be registered before redirect");
    }

    @And("a new user account should be created with username {string}")
    public void a_new_user_account_should_be_created_with_username(String username) {
        assertNotNull(registeredUser, "User should be registered");
        assertEquals(username, registeredUser.getUsername());
    }

    @And("a new user account should be created")
    public void a_new_user_account_should_be_created() {
        assertNotNull(registeredUser, "User should be registered successfully");
    }

    @And("I should remain on the registration page")
    public void i_should_remain_on_the_registration_page() {
        assertNotNull(caughtException, "Should remain on page due to error");
    }

    @And("no new user account should be created")
    public void no_new_user_account_should_be_created() {
        assertNull(registeredUser, "No user should be created");
    }

    @Given("a user already exists with username {string}")
    public void a_user_already_exists_with_username(String username) {
        // This would typically involve database setup
        // For now, we simulate by creating a user in the service
        try {
            User existingUser = new User(username, "existing@example.com", "Password123");
            userService.registerUser(existingUser);
        } catch (Exception e) {
            // User might already exist, which is fine for our test
        }
    }

    @Given("a user already exists with email {string}")
    public void a_user_already_exists_with_email(String email) {
        // This would typically involve database setup
        try {
            User existingUser = new User("existinguser", email, "Password123");
            userService.registerUser(existingUser);
        } catch (Exception e) {
            // User might already exist, which is fine for our test
        }
    }
}
