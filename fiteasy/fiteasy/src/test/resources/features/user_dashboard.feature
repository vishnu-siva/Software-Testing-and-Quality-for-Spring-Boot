Feature: User Dashboard
  As a logged-in user
  I want to view my fitness dashboard
  So that I can see my progress, workout plans, and BMI results

  Background:
    Given the FitEasy application is running
    And I am logged in as user "testuser" with ID "1" for dashboard
    And I have completed my user profile details
    And I have created 2 workout plans
    And I have 1 BMI result

  Scenario: View complete dashboard with all data
    Given I am logged in as dashboard user "testuser"
    When I navigate to the dashboard page
    Then I should see my user statistics:
      | Statistic        | Value |
      | Username         | testuser |
      | Profile Complete | 1     |
      | Workout Plans    | 2     |
      | BMI Results      | 1     |
    And I should see a welcome message "Welcome back, testuser!"
    And I should see quick action buttons for:
      | Action           |
      | Create Workout Plan |
      | View My Plans    |
      | Update Profile   |

  Scenario: View dashboard with incomplete profile
    Given I am logged in as dashboard user "testuser"
    And my user profile is incomplete (missing name and age)
    When I navigate to the dashboard page
    Then I should see my user statistics:
      | Statistic        | Value |
      | Profile Complete | 0     |
      | Workout Plans    | 2     |
      | BMI Results      | 1     |
    And I should see a notification "Complete your profile to get personalized recommendations"
    And I should see a "Complete Profile" button

  Scenario: View dashboard with no workout plans
    Given I am logged in as dashboard user "testuser"
    And I have no workout plans created
    When I navigate to the dashboard page
    Then I should see my user statistics:
      | Statistic        | Value |
      | Profile Complete | 1     |
      | Workout Plans    | 0     |
      | BMI Results      | 0     |
    And I should see a message "Start your fitness journey by creating your first workout plan"
    And I should see a prominent "Create Workout Plan" button

  Scenario: View dashboard with no BMI results
    Given I am logged in as dashboard user "testuser"
    And I have workout plans but no BMI calculations
    When I navigate to the dashboard page
    Then I should see my user statistics:
      | Statistic        | Value |
      | Profile Complete | 1     |
      | Workout Plans    | 2     |
      | BMI Results      | 0     |
    And I should see a message "Add your height and weight to get BMI calculations"
    And I should see a "Calculate BMI" button

  Scenario: Navigate to workout plans from dashboard
    Given I am logged in as dashboard user "testuser"
    And I am on the dashboard page
    When I click on "View My Plans"
    Then I should be redirected to the workout plans page
    And I should see all my workout plans listed

  Scenario: Navigate to profile update from dashboard
    Given I am logged in as dashboard user "testuser"
    And I am on the dashboard page
    When I click on "Update Profile"
    Then I should be redirected to the user details page
    And I should see my current profile information

  Scenario: View dashboard as new user
    Given I am logged in as a new user with no data
    When I navigate to the dashboard page
    Then I should see my user statistics:
      | Statistic        | Value |
      | Profile Complete | 0     |
      | Workout Plans    | 0     |
      | BMI Results      | 0     |
    And I should see a welcome message "Welcome to FitEasy!"
    And I should see onboarding suggestions:
      | Suggestion                    |
      | Complete your profile        |
      | Create your first workout plan|
      | Calculate your BMI           |
