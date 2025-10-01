# FitEasy BDD Feature Files

This directory contains Behavior-Driven Development (BDD) feature files written in Gherkin syntax for the FitEasy fitness application.

## Feature Files Overview

### 1. **user_registration.feature**
- **User Story**: As a new user, I want to register for a FitEasy account so that I can access the fitness application and track my workouts
- **Scenarios**: 7 scenarios covering successful registration, validation errors, and edge cases
- **Key Validations**: Email format, password strength, duplicate username/email, required fields

### 2. **user_login.feature**
- **User Story**: As a registered user, I want to log into my FitEasy account so that I can access my personalized fitness dashboard and workout plans
- **Scenarios**: 7 scenarios covering successful login, authentication failures, and security features
- **Key Features**: Credential validation, error handling, multiple failed attempts protection

### 3. **workout_plan_creation.feature**
- **User Story**: As a registered user, I want to create a personalized workout plan so that I can track my fitness goals and get BMI calculations
- **Scenarios**: 8 scenarios covering successful creation, validation errors, and BMI calculations
- **Key Features**: Input validation, BMI calculation, user existence verification

### 4. **bmi_calculation.feature**
- **User Story**: As a user creating a workout plan, I want the system to automatically calculate my BMI so that I can understand my current fitness level and get appropriate workout recommendations
- **Scenarios**: 8 scenarios covering BMI calculations for different weight categories and edge cases
- **Key Features**: BMI formula validation, category classification, error handling

### 5. **user_dashboard.feature**
- **User Story**: As a logged-in user, I want to view my fitness dashboard so that I can see my progress, workout plans, and BMI results
- **Scenarios**: 6 scenarios covering dashboard display for different user states
- **Key Features**: Statistics display, navigation, onboarding for new users

### 6. **workout_plan_management.feature**
- **User Story**: As a logged-in user, I want to manage my workout plans so that I can create, view, update, and delete my fitness routines
- **Scenarios**: 8 scenarios covering CRUD operations, search, filtering, and export
- **Key Features**: Plan management, search/filter functionality, data export

## Gherkin Syntax Elements Used

### **Given-When-Then Structure**
- **Given**: Sets up the initial context and preconditions
- **When**: Describes the action or event that triggers the behavior
- **Then**: Defines the expected outcome or result

### **Background**
- Provides common setup steps shared across scenarios
- Reduces duplication in feature files

### **Scenario Outlines**
- Used for testing multiple data combinations
- Examples tables provide test data variations

### **Data Tables**
- Used for structured input/output data
- Makes scenarios more readable and maintainable

## Key BDD Benefits Demonstrated

1. **Business Readability**: Feature files are written in plain English that stakeholders can understand
2. **Living Documentation**: Features serve as executable specifications
3. **Test Coverage**: Comprehensive scenarios cover happy paths, edge cases, and error conditions
4. **Collaboration**: Features facilitate communication between developers, testers, and business stakeholders
5. **Regression Testing**: Scenarios can be automated to prevent regressions

## Total Coverage

- **6 Feature Files**
- **44 Scenarios** across all features
- **Multiple User Stories** covering core application functionality
- **Comprehensive Edge Cases** and error handling
- **Data-Driven Testing** with scenario outlines and examples

## Next Steps

These feature files can be used with BDD testing frameworks like:
- **Cucumber** for Java
- **SpecFlow** for .NET
- **Behave** for Python
- **Jest-Cucumber** for JavaScript

The features provide a solid foundation for automated acceptance testing and ensure the application meets business requirements.
