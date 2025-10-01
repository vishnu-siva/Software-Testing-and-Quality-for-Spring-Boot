Feature: BMI Calculation and Analysis
  As a user creating a workout plan
  I want the system to automatically calculate my BMI
  So that I can understand my current fitness level and get appropriate workout recommendations

  Background:
    Given the FitEasy application is running
    And I am logged in as a registered user

  Scenario: BMI calculation for normal weight person
    Given I am creating a workout plan
    When I enter my physical measurements:
      | Field         | Value  |
      | Height (cm)   | 175.0  |
      | Weight (kg)   | 70.0   |
    And I submit the workout plan
    Then my BMI should be calculated as "22.86"
    And my BMI category should be "Normal weight"
    And I should see a BMI message "Your BMI indicates a healthy weight range"

  Scenario: BMI calculation for underweight person
    Given I am creating a workout plan
    When I enter my physical measurements:
      | Field         | Value  |
      | Height (cm)   | 175.0  |
      | Weight (kg)   | 55.0   |
    And I submit the workout plan
    Then my BMI should be calculated as "17.96"
    And my BMI category should be "Underweight"
    And I should see a BMI message "Your BMI indicates you may be underweight. Consider consulting a healthcare provider."

  Scenario: BMI calculation for overweight person
    Given I am creating a workout plan
    When I enter my physical measurements:
      | Field         | Value  |
      | Height (cm)   | 175.0  |
      | Weight (kg)   | 85.0   |
    And I submit the workout plan
    Then my BMI should be calculated as "27.76"
    And my BMI category should be "Overweight"
    And I should see a BMI message "Your BMI indicates you are overweight. Consider a balanced diet and regular exercise."

  Scenario: BMI calculation for obese person
    Given I am creating a workout plan
    When I enter my physical measurements:
      | Field         | Value  |
      | Height (cm)   | 175.0  |
      | Weight (kg)   | 100.0  |
    And I submit the workout plan
    Then my BMI should be calculated as "32.65"
    And my BMI category should be "Obese"
    And I should see a BMI message "Your BMI indicates obesity. Please consult a healthcare provider for guidance."

  Scenario: BMI calculation fails with invalid height
    Given I am creating a workout plan
    When I enter my physical measurements:
      | Field         | Value  |
      | Height (cm)   | -175.0 |
      | Weight (kg)   | 70.0   |
    And I try to submit the workout plan
    Then I should see a BMI error message "Height must be positive"
    And no BMI calculation should be performed

  Scenario: BMI calculation fails with invalid weight
    Given I am creating a workout plan
    When I enter my physical measurements:
      | Field         | Value  |
      | Height (cm)   | 175.0  |
      | Weight (kg)   | -70.0  |
    And I try to submit the workout plan
    Then I should see a BMI error message "Weight must be positive"
    And no BMI calculation should be performed

  Scenario: BMI calculation with zero values
    Given I am creating a workout plan
    When I enter my physical measurements:
      | Field         | Value |
      | Height (cm)   | 0.0   |
      | Weight (kg)   | 70.0  |
    And I try to submit the workout plan
    Then I should see a BMI error message "Height and weight must be positive numbers"
    And no BMI calculation should be performed

  Scenario Outline: BMI calculation for various height and weight combinations
    Given I am creating a workout plan
    When I enter my physical measurements:
      | Field         | Value  |
      | Height (cm)   | <height> |
      | Weight (kg)   | <weight> |
    And I submit the workout plan
    Then my BMI should be calculated as "<expected_bmi>"
    And my BMI category should be "<bmi_category>"

    Examples:
      | height | weight | expected_bmi | bmi_category |
      | 150.0  | 45.0   | 20.00       | Normal weight|
      | 160.0  | 50.0   | 19.53       | Normal weight|
      | 170.0  | 60.0   | 20.76       | Normal weight|
      | 180.0  | 70.0   | 21.60       | Normal weight|
      | 190.0  | 80.0   | 22.16       | Normal weight|
      | 175.0  | 50.0   | 16.33       | Underweight  |
      | 175.0  | 90.0   | 29.39       | Overweight   |
      | 175.0  | 110.0  | 35.92       | Obese        |

  Scenario: BMI calculation with decimal values
    Given I am creating a workout plan
    When I enter my physical measurements:
      | Field         | Value  |
      | Height (cm)   | 175.5  |
      | Weight (kg)   | 70.5   |
    And I submit the workout plan
    Then my BMI should be calculated as "22.89"
    And my BMI category should be "Normal weight"
    And the BMI should be rounded to 2 decimal places
