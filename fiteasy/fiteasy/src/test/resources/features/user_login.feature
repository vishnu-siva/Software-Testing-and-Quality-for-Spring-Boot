Feature: User Login
  As a registered user
  I want to log into my FitEasy account
  So that I can access my personalized fitness dashboard and workout plans

  Background:
    Given the FitEasy application is running
    And a user exists with username "testuser" and password "Password123"

  Scenario: Successful login with valid credentials
    Given I am on the login page
    When I enter the following login details:
      | Field    | Value      |
      | Username | testuser   |
      | Password | Password123|
    And I click the "Login" button
    Then I should see a login success message "Login successful"
    And I should be redirected to the dashboard page
    And I should see my username "testuser" displayed

  Scenario: Login fails with incorrect username
    Given I am on the login page
    When I enter the following login details:
      | Field    | Value        |
      | Username | wronguser    |
      | Password | Password123  |
    And I click the "Login" button
    Then I should see a login error message "Invalid username or password"
    And I should remain on the login page
    And I should not be redirected to the dashboard

  Scenario: Login fails with incorrect password
    Given I am on the login page
    When I enter the following login details:
      | Field    | Value      |
      | Username | testuser   |
      | Password | WrongPass  |
    And I click the "Login" button
    Then I should see a login error message "Invalid username or password"
    And I should remain on the login page
    And I should not be redirected to the dashboard

  Scenario: Login fails with empty username
    Given I am on the login page
    When I enter the following login details:
      | Field    | Value      |
      | Username |            |
      | Password | Password123|
    And I click the "Login" button
    Then I should see a login error message "Username is required"
    And I should remain on the login page

  Scenario: Login fails with empty password
    Given I am on the login page
    When I enter the following login details:
      | Field    | Value    |
      | Username | testuser |
      | Password |          |
    And I click the "Login" button
    Then I should see a login error message "Password is required"
    And I should remain on the login page

  Scenario: Login fails with both fields empty
    Given I am on the login page
    When I enter the following login details:
      | Field    | Value |
      | Username |       |
      | Password |       |
    And I click the "Login" button
    Then I should see a login error message "Username and password are required"
    And I should remain on the login page

  Scenario: Login with case-sensitive username
    Given I am on the login page
    When I enter the following login details:
      | Field    | Value      |
      | Username | TestUser   |
      | Password | Password123|
    And I click the "Login" button
    Then I should see a login error message "Invalid username or password"
    And I should remain on the login page

  Scenario: Multiple failed login attempts
    Given I am on the login page
    When I attempt to login 3 times with incorrect credentials
    Then I should see a login error message "Too many failed attempts. Please try again later."
    And the login form should be disabled