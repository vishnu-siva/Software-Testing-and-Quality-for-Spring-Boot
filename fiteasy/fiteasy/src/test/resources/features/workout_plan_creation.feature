Feature: Workout Plan Creation
  As a registered user
  I want to create a personalized workout plan
  So that I can track my fitness goals and get BMI calculations

  Background:
    Given the FitEasy application is running
    And I am logged in as a registered user with ID "1"
    And I am on the workout plan creation page

  Scenario: Successful workout plan creation with BMI calculation
    Given I am logged in as user with ID "1" for workout creation
    When I enter the following workout plan details:
      | Field         | Value                    |
      | Age           | 25                       |
      | Gender        | Male                      |
      | Height (cm)   | 175.0                    |
      | Weight (kg)   | 70.0                     |
      | Trainer       | John Doe                  |
      | Gym Name      | FitGym                    |
      | Time in Gym   | 1 hour                   |
      | Workout       | Push-ups, Pull-ups, Squats|
      | Reps/Sets     | 3 sets of 10 reps         |
    And I press the "Create Workout Plan" button on the workout plan page
    Then I should see a workout creation success message "Workout plan created successfully"
    And the workout plan should be saved with BMI data "22.86"
    And I should be redirected to my workout plans page

  Scenario: Workout plan creation fails with invalid age
    Given I am logged in as user with ID "1" for workout creation
    When I enter the following workout plan details:
      | Field         | Value                    |
      | Age           | -5                       |
      | Gender        | Male                      |
      | Height (cm)   | 175.0                    |
      | Weight (kg)   | 70.0                     |
      | Workout       | Push-ups, Pull-ups, Squats|
    And I press the "Create Workout Plan" button on the workout plan page
    Then I should see a workout creation error message "Age must be positive"
    And I should remain on the workout plan creation page
    And no workout plan should be created

  Scenario: Workout plan creation fails with invalid height
    Given I am logged in as user with ID "1" for workout creation
    When I enter the following workout plan details:
      | Field         | Value                    |
      | Age           | 25                       |
      | Gender        | Male                      |
      | Height (cm)   | -175.0                   |
      | Weight (kg)   | 70.0                     |
      | Workout       | Push-ups, Pull-ups, Squats|
    And I press the "Create Workout Plan" button on the workout plan page
    Then I should see a workout creation error message "Height must be positive"
    And I should remain on the workout plan creation page
    And no workout plan should be created

  Scenario: Workout plan creation fails with invalid weight
    Given I am logged in as user with ID "1" for workout creation
    When I enter the following workout plan details:
      | Field         | Value                    |
      | Age           | 25                       |
      | Gender        | Male                      |
      | Height (cm)   | 175.0                    |
      | Weight (kg)   | -70.0                    |
      | Workout       | Push-ups, Pull-ups, Squats|
    And I press the "Create Workout Plan" button on the workout plan page
    Then I should see a workout creation error message "Weight must be positive"
    And I should remain on the workout plan creation page
    And no workout plan should be created

  Scenario: Workout plan creation fails with empty workout description
    Given I am logged in as user with ID "1" for workout creation
    When I enter the following workout plan details:
      | Field         | Value                    |
      | Age           | 25                       |
      | Gender        | Male                      |
      | Height (cm)   | 175.0                    |
      | Weight (kg)   | 70.0                     |
      | Workout       |                          |
    And I press the "Create Workout Plan" button on the workout plan page
    Then I should see a workout creation error message "Workout description is required"
    And I should remain on the workout plan creation page
    And no workout plan should be created

  Scenario: Workout plan creation fails for non-existent user
    Given I am not logged in or user with ID "999" does not exist
    When I try to create a workout plan with user ID "999"
    Then I should see a workout creation error message "User not found with id: 999"
    And no workout plan should be created

  Scenario Outline: BMI calculation for different height and weight combinations
    Given I am logged in as user with ID "1" for workout creation
    When I enter the following workout plan details:
      | Field         | Value                    |
      | Age           | 25                       |
      | Gender        | Male                      |
      | Height (cm)   | <height>                 |
      | Weight (kg)   | <weight>                 |
      | Workout       | Push-ups, Pull-ups, Squats|
    And I click the workout "Create Workout Plan" button
    Then the workout plan should be saved with BMI data "<expected_bmi>"
    And the BMI category should be "<bmi_category>"

    Examples:
      | height | weight | expected_bmi | bmi_category |
      | 175.0  | 60.0   | 19.59       | Normal weight|
      | 175.0  | 70.0   | 22.86       | Normal weight|
      | 175.0  | 80.0   | 26.12       | Overweight   |
      | 175.0  | 90.0   | 29.39       | Overweight   |
      | 175.0  | 100.0  | 32.65       | Obese        |
      | 160.0  | 50.0   | 19.53       | Normal weight|
      | 180.0  | 75.0   | 23.15       | Normal weight|
