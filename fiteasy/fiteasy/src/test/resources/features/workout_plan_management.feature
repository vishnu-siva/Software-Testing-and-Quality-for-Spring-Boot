Feature: Workout Plan Management
  As a logged-in user
  I want to manage my workout plans
  So that I can create, view, update, and delete my fitness routines

  Background:
    Given the FitEasy application is running
    And I am logged in as user "testuser" with ID "1"
    And I have created 2 workout plans previously

  Scenario: View all my workout plans
    Given I am logged in as user "testuser"
    When I navigate to the "My Workout Plans" page
    Then I should see a list of my workout plans:
      | Plan ID | Created Date | BMI Data | Trainer    |
      | 1       | 2024-01-15   | 22.86    | John Doe   |
      | 2       | 2024-01-20   | 23.15    | Jane Smith |
    And I should see action buttons for each plan:
      | Action    |
      | View      |
      | Edit      |
      | Delete    |

  Scenario: View detailed workout plan
    Given I am logged in as user "testuser"
    And I am on the "My Workout Plans" page
    When I click "View" on workout plan ID "1"
    Then I should see the detailed workout plan information:
      | Field         | Value                    |
      | Age           | 25                       |
      | Gender        | Male                      |
      | Height (cm)   | 175.0                    |
      | Weight (kg)   | 70.0                     |
      | BMI           | 22.86                    |
      | BMI Category  | Normal weight            |
      | Trainer       | John Doe                 |
      | Gym Name      | FitGym                   |
      | Time in Gym   | 1 hour                   |
      | Workout       | Push-ups, Pull-ups, Squats|
      | Reps/Sets     | 3 sets of 10 reps         |

  Scenario: Update existing workout plan
    Given I am logged in as user "testuser"
    And I am on the "My Workout Plans" page
    When I click "Edit" on workout plan ID "1"
    And I update the following fields:
      | Field         | New Value                |
      | Weight (kg)   | 75.0                     |
      | Workout       | Push-ups, Pull-ups, Squats, Lunges |
      | Reps/Sets     | 4 sets of 12 reps         |
    And I click "Update Workout Plan"
    Then I should see a workout plan success message "Workout plan updated successfully"
    And the BMI should be recalculated to "24.49"
    And I should be redirected to the workout plans list

  Scenario: Delete workout plan
    Given I am logged in as user "testuser"
    And I am on the "My Workout Plans" page
    When I click "Delete" on workout plan ID "2"
    And I confirm the deletion
    Then I should see a workout plan success message "Workout plan deleted successfully"
    And the workout plan should be removed from the list
    And I should be redirected to the workout plans list

  Scenario: Delete workout plan with confirmation dialog
    Given I am logged in as user "testuser"
    And I am on the "My Workout Plans" page
    When I click "Delete" on workout plan ID "1"
    Then I should see a confirmation dialog "Are you sure you want to delete this workout plan?"
    And I should see buttons "Cancel" and "Delete"
    When I click "Cancel"
    Then the dialog should close
    And the workout plan should remain in the list

  Scenario: Search workout plans by trainer
    Given I am logged in as user "testuser"
    And I am on the "My Workout Plans" page
    When I enter "John Doe" in the trainer search field
    And I click "Search"
    Then I should see only workout plans with trainer "John Doe"
    And I should see 1 workout plan in the results

  Scenario: Filter workout plans by BMI category
    Given I am logged in as user "testuser"
    And I am on the "My Workout Plans" page
    When I select "Normal weight" from the BMI category filter
    And I click "Apply Filter"
    Then I should see only workout plans with BMI category "Normal weight"
    And I should see 2 workout plans in the results

  Scenario: View workout plan statistics
    Given I am logged in as user "testuser"
    And I am on the "My Workout Plans" page
    Then I should see workout plan statistics:
      | Statistic           | Value |
      | Total Plans         | 2     |
      | Plans with BMI      | 2     |
      | Average BMI         | 23.01 |
      | Most Common Trainer | John Doe |
      | Most Common Gym     | FitGym |

  Scenario: Export workout plans
    Given I am logged in as user "testuser"
    And I am on the "My Workout Plans" page
    When I click "Export Plans"
    Then I should see a download dialog
    And a CSV file should be generated with all my workout plans
    And the file should contain the following columns:
      | Column        |
      | Plan ID       |
      | Created Date  |
      | Age           |
      | Gender        |
      | Height        |
      | Weight        |
      | BMI           |
      | BMI Category  |
      | Trainer       |
      | Gym Name      |
      | Workout       |
      | Reps/Sets     |
