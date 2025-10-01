# Signup UI Tests Documentation

## Overview

This document provides comprehensive documentation for the Selenium UI test suite designed to test signup functionality and existing user workflows. The test suite follows the same pattern as the existing LoginPage tests and is specifically configured to work with your existing registered user in MySQL.

## Test Architecture

### Structure
```
src/test/java/com/fiteasy/selenium/
├── pages/
│   ├── LoginPage.java           # Already exists
│   ├── SignupPage.java          # NEW - Signup page object
│   └── UserPage.java            # Already exists
├── tests/
│   ├── SignupUITest.java        # NEW - Main signup tests  
│   ├── SignupValidationTest.java # NEW - Validation edge cases
│   ├── ExistingUserTest.java    # NEW - Tests with your registered user
│   └── LoginUITest.java         # Already exists
└── base/
    └── BaseSeleniumTest.java    # Already exists
```

### Technology Stack
- **Language**: Java
- **Test Framework**: TestNG
- **Browser Automation**: Selenium WebDriver
- **Design Pattern**: Page Object Model (POM)
- **Build Tool**: Maven

## Existing User Integration

### Your Registered User Details
```
Username: Vishnuha
Email: vishnusiva885588@gmail.com
Password: 11111111
```

All tests are designed to work with this existing user and avoid conflicts by:
- Testing duplicate prevention with your existing credentials
- Using dynamic timestamps for new test data
- Providing comprehensive login testing with your user

## Test Files Description

### 1. SignupPage.java
**Purpose**: Page Object Model for signup functionality following LoginPage pattern.

**Key Features**:
- Multiple navigation strategies to find signup pages
- Robust element location with fallback selectors
- Support for both simple and confirm-password signup forms
- Comprehensive validation and error detection
- Smart form interaction with field availability checks

**Main Methods**:
```java
// Navigation
navigateToSignupPage(String baseUrl);
clickLoginLink();  // Navigate to login from signup

// Form Interaction
signup(String username, String email, String password);
signupWithConfirmation(String username, String email, String password, String confirmPassword);
enterUsername(String username);
enterEmail(String email);
enterPassword(String password);
enterConfirmPassword(String confirmPassword);

// Validation
isSignupSuccessful();
isErrorMessageDisplayed();
hasValidationErrors();
getErrorMessage();
getValidationErrors();
```

### 2. SignupUITest.java
**Purpose**: Main comprehensive signup test suite covering all signup scenarios.

**Test Categories**:

#### Basic Signup Tests (Priority 1-3)
- `testSignupPageAccess()` - Page accessibility and navigation
- `testValidSignup()` - Valid new user registration
- `testSignupWithPasswordConfirmation()` - Password confirmation flow

#### Duplicate Prevention Tests (Priority 4-5) 
- `testSignupWithExistingUsername()` - Prevention with your username "Vishnuha"
- `testSignupWithExistingEmail()` - Prevention with your email

#### Validation Tests (Priority 6-8)
- `testMismatchedPasswordConfirmation()` - Password mismatch detection
- `testInvalidEmailFormats()` - Email format validation (10+ test cases)
- `testEmptyRequiredFields()` - Empty field validation

#### Security Tests (Priority 9)
- `testWeakPasswordValidation()` - Password strength testing (10+ cases)

#### Edge Case Tests (Priority 10-11)
- `testSpecialCharactersInUsername()` - Special character handling
- `testFieldLengthLimits()` - Length boundary testing

#### User Experience Tests (Priority 12-13)
- `testSignupLoginNavigation()` - Navigation between signup/login
- `testSignupFormUsability()` - Form accessibility testing

#### Integration Test (Priority 14)
- `testExistingUserLoginAfterSignupTests()` - Verify your user still works

### 3. SignupValidationTest.java
**Purpose**: Specialized validation testing for edge cases and security.

**Test Categories**:

#### Advanced Username Validation
- Length constraints (1 char to 1000+ chars)
- Special characters and patterns
- Security injection attempts

#### Advanced Email Validation
- Complex email formats (plus addressing, subdomains, etc.)
- Case sensitivity testing
- Boundary conditions

#### Password Security Testing
- Complexity requirements validation
- Password confirmation variations
- Security input handling

#### Duplicate Data Validation
- Case variations of existing username/email
- Whitespace and character variations

#### Security Testing
- XSS injection attempts
- SQL injection patterns
- CSRF protection testing
- Boundary limit testing

### 4. ExistingUserTest.java
**Purpose**: Tests specifically for your existing registered MySQL user.

**Test Categories**:

#### Login Functionality
- `testExistingUserLogin()` - Login with Vishnuha/11111111
- `testExistingUserWrongPassword()` - Wrong password handling
- `testExistingUsernameCaseSensitivity()` - Case sensitivity testing

#### Signup Prevention
- `testSignupPreventionExistingUsername()` - Prevent duplicate "Vishnuha"
- `testSignupPreventionExistingEmail()` - Prevent duplicate email

#### Profile Management  
- `testExistingUserProfileAccess()` - Profile page access after login
- `testExistingUserProfileUpdate()` - Profile update functionality

#### Session Management
- `testExistingUserSessionPersistence()` - Session maintenance
- `testExistingUserLogout()` - Logout functionality

#### Complete Workflow
- `testCompleteExistingUserWorkflow()` - End-to-end user journey

## Test Data Strategy

### Dynamic Test Data
All tests use dynamic data to prevent conflicts:
```java
private final String testTimestamp = String.valueOf(System.currentTimeMillis() / 1000);
private final String baseTestUsername = "newuser" + testTimestamp;
private final String baseTestEmail = "newuser" + testTimestamp + "@example.com";
```

### Existing User Integration
Tests reference your existing credentials as constants:
```java
private static final String EXISTING_USERNAME = "Vishnuha";
private static final String EXISTING_EMAIL = "vishnusiva885588@gmail.com";
private static final String EXISTING_PASSWORD = "11111111";
```

## Test Execution

### Running Individual Test Files
```bash
# Run signup UI tests
./mvnw test -Dtest=SignupUITest

# Run signup validation tests  
./mvnw test -Dtest=SignupValidationTest

# Run existing user tests
./mvnw test -Dtest=ExistingUserTest

# Run all signup-related tests
./mvnw test -Dtest="Signup*Test,ExistingUserTest"
```

### Running Specific Test Methods
```bash
# Test signup page access
./mvnw test -Dtest=SignupUITest#testSignupPageAccess

# Test existing user login
./mvnw test -Dtest=ExistingUserTest#testExistingUserLogin

# Test duplicate prevention
./mvnw test -Dtest=SignupUITest#testSignupWithExistingUsername
```

### Running All Login and Signup Tests
```bash
# Run all authentication-related tests
./mvnw test -Dtest="*Login*Test,*Signup*Test,ExistingUserTest"
```

## Expected Test Results

### Successful Scenarios
- ✅ **Signup Page Access**: Navigate to signup page successfully
- ✅ **Valid Signup**: New users can register (if backend allows)
- ✅ **Existing User Login**: Vishnuha can login with 11111111
- ✅ **Duplicate Prevention**: Existing username/email rejected in signup
- ✅ **Form Validation**: Invalid inputs properly handled
- ✅ **Navigation**: Signup ↔ Login page navigation works
- ✅ **Security**: Injection attempts handled gracefully

### Expected Validations
- ⚠ **Weak Passwords**: Should be rejected or warned
- ⚠ **Invalid Emails**: Should be rejected with validation errors
- ⚠ **Empty Fields**: Required field validation should trigger
- ⚠ **Duplicate Data**: Should prevent signup with existing user data

### Acceptable Variations
- **Lenient Validation**: Some validations may be less strict
- **UI Differences**: Frontend may use different element selectors
- **Feature Availability**: Some features (like profile) may not be implemented yet

## Configuration and Customization

### Base URLs
```java
protected static final String BASE_URL = "http://localhost:3000";
protected static final String API_BASE_URL = "http://localhost:8080/api";
```

### Adapting to Your Frontend

#### Update Element Selectors
Modify `SignupPage.java` if your frontend uses different selectors:
```java
@FindBy(css = "input[name='username'], #username, .username-field")
private WebElement usernameField;

@FindBy(css = "input[name='email'], #email, input[type='email']")
private WebElement emailField;
```

#### Update Navigation URLs
Change signup URL patterns in `SignupPage.java`:
```java
private static final String SIGNUP_URL = "/signup";  // Change to your URL

String[] signupUrls = {
    "/signup", "/register", "/sign-up"  // Add your signup URLs
};
```

### Adding Your Validation Rules
Update data providers in test classes to match your validation rules:
```java
@DataProvider(name = "weakPasswords")
public Object[][] weakPasswords() {
    return new Object[][] {
        {"123", "too short", false},           // Adjust based on your rules
        {"MyPassword123!", "complex", true}    // Add your requirements
    };
}
```

## Debugging and Troubleshooting

### Common Issues

#### 1. Element Not Found
```
⚠ Could not find signup form at: http://localhost:3000/signup
```
**Solution**: Update element selectors in `SignupPage.java` to match your frontend

#### 2. Navigation Issues
```
⚠ No direct signup page found, trying base URL
```
**Solution**: Add your signup URL patterns to the `signupUrls` array

#### 3. Validation Differences
```
⚠ Weak password accepted (lenient validation)
```
**Solution**: This is informational - adjust test expectations based on your validation rules

#### 4. Existing User Issues
```
❌ Existing registered user should be able to login successfully
```
**Solution**: Verify your application is running and user exists in MySQL

### Enhanced Debugging

All tests include comprehensive logging:
```
=== Starting Signup UI Test ===
🧪 Testing valid user signup...
✓ Entered username: newuser1234567890
✓ Entered email: valid1234567890@test.com
✓ Entered password: [MASKED]
✓ Clicked signup button
✅ Valid signup test completed
```

### Screenshots and Evidence
The base test class supports screenshot capture (can be enhanced as needed).

## Test Coverage Summary

### Signup Functionality
- **Page Access**: ✅ Covered
- **Form Interaction**: ✅ Covered  
- **Validation**: ✅ Comprehensive
- **Error Handling**: ✅ Covered
- **Navigation**: ✅ Covered
- **Security**: ✅ Basic coverage

### Existing User Integration
- **Login Testing**: ✅ Comprehensive
- **Duplicate Prevention**: ✅ Covered
- **Profile Access**: ✅ Covered
- **Session Management**: ✅ Basic coverage
- **Workflow Integration**: ✅ Covered

### Validation Coverage
- **Username**: Length, characters, duplicates, injection
- **Email**: Format, duplicates, case sensitivity
- **Password**: Complexity, confirmation, security
- **Form**: Empty fields, boundaries, usability
- **Security**: XSS, SQL injection, CSRF

## Integration with Existing Tests

### Compatibility
- Uses same base classes as existing LoginPage tests
- Follows same patterns and conventions
- Reuses existing WebDriver configuration
- Compatible with existing test infrastructure

### Combined Test Execution
```bash
# Run all UI tests (Login + Signup + User)
./mvnw test -Dtest="*UI*Test"

# Run authentication flow tests
./mvnw test -Dtest="*Login*Test,*Signup*Test,ExistingUserTest"
```

## Maintenance and Updates

### Regular Maintenance
1. **Update Selectors**: When frontend changes
2. **Update URLs**: When routes change  
3. **Update Validations**: When business rules change
4. **Update Test Data**: If needed for edge cases

### Performance Considerations
- Tests use dynamic data to avoid conflicts
- Independent test isolation
- Comprehensive cleanup not needed (tests are isolated)

## Quick Start Guide

1. **Ensure Prerequisites**:
   - Application running at `http://localhost:3000`
   - MySQL with your registered user (Vishnuha)
   - Chrome browser and ChromeDriver

2. **Run Key Tests**:
   ```bash
   # Test your existing user works
   ./mvnw test -Dtest=ExistingUserTest#testExistingUserLogin
   
   # Test signup page access
   ./mvnw test -Dtest=SignupUITest#testSignupPageAccess
   
   # Test duplicate prevention
   ./mvnw test -Dtest=SignupUITest#testSignupWithExistingUsername
   ```

3. **Review Results**:
   - Check console output for detailed logs
   - Look for ✅ success and ⚠ warnings
   - Address any ❌ failures based on your implementation

4. **Customize as Needed**:
   - Update element selectors for your frontend
   - Adjust validation expectations
   - Add additional test scenarios

---

This test suite provides comprehensive signup functionality testing while ensuring compatibility with your existing user data. The modular design allows for easy customization and maintenance as your application evolves.