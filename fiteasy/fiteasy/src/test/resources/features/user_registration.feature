Feature: User Registration
  As a new user
  I want to register for a FitEasy account
  So that I can access the fitness application and track my workouts

  Background:
    Given the FitEasy application is running
    And no user exists with the email "test@example.com"

  Scenario: Successful user registration with valid data
    Given I am on the registration page
    When I enter the following registration details:
      | Field    | Value              |
      | Username | maintestuser       |
      | Email    | main@example.com   |
      | Password | Password123        |
    And I click the registration "Register" button
    Then I should see a registration success message "Registration successful"
    And I should be redirected to the login page
    And a new user account should be created with username "maintestuser"

  Scenario: Registration fails with duplicate username
    Given a user already exists with username "existinguser"
    And I am on the registration page
    When I enter the following registration details:
      | Field    | Value              |
      | Username | existinguser       |
      | Email    | newuser@example.com|
      | Password | Password123        |
    And I click the registration "Register" button
    Then I should see a registration error message "Username already exists. Please choose a different username."
    And I should remain on the registration page
    And no new user account should be created

  Scenario: Registration fails with duplicate email
    Given a user already exists with email "existing@example.com"
    And I am on the registration page
    When I enter the following registration details:
      | Field    | Value              |
      | Username | newuser            |
      | Email    | existing@example.com|
      | Password | Password123        |
    And I click the registration "Register" button
    Then I should see a registration error message "Email already exists. Please use a different email address."
    And I should remain on the registration page
    And no new user account should be created

  Scenario: Registration fails with invalid email format
    Given I am on the registration page
    When I enter the following registration details:
      | Field    | Value              |
      | Username | emailtestuser      |
      | Email    | invalid-email      |
      | Password | Password123        |
    And I click the registration "Register" button
    Then I should see a registration error message "Invalid email format"
    And I should remain on the registration page
    And no new user account should be created

  Scenario: Registration fails with weak password
    Given I am on the registration page
    When I enter the following registration details:
      | Field    | Value              |
      | Username | passwordtestuser   |
      | Email    | weak@example.com   |
      | Password | 123                |
    And I click the registration "Register" button
    Then I should see a registration error message "Password must be at least 8 characters long"
    And I should remain on the registration page
    And no new user account should be created

  Scenario: Registration fails with empty required fields
    Given I am on the registration page
    When I enter the following registration details:
      | Field    | Value              |
      | Username |                    |
      | Email    | empty@example.com  |
      | Password | Password123        |
    And I click the registration "Register" button
    Then I should see a registration error message "Username is required"
    And I should remain on the registration page
    And no new user account should be created

  Scenario Outline: Registration with various valid email formats
    Given I am on the registration page
    When I enter the following registration details:
      | Field    | Value              |
      | Username | <username>         |
      | Email    | <email>            |
      | Password | Password123        |
    And I click the registration "Register" button
    Then I should see a registration success message "Registration successful"
    And a new user account should be created

    Examples:
      | username     | email                    |
      | testuser1    | user@example.com         |
      | testuser2    | user.name@example.com    |
      | testuser3    | user+tag@example.co.uk   |
      | testuser4    | user123@test-domain.org  |
