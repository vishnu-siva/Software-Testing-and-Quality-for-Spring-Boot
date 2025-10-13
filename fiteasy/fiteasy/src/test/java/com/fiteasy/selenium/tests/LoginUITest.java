package com.fiteasy.selenium.tests;

import com.fiteasy.selenium.base.BaseSeleniumTest;
import com.fiteasy.selenium.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginUITest extends BaseSeleniumTest {
    
    private LoginPage loginPage;
    
    @BeforeMethod
    @Override
    public void setUp() {
        super.setUp();
        loginPage = new LoginPage(driver, wait);
    }
    
    @Test(priority = 0, description = "Debug test to check login page structure")
    public void debugLoginPageStructure() {
        System.out.println("=== DEBUG LOGIN PAGE STRUCTURE ===");
        
        // Navigate to login page
        loginPage.navigateToLoginPage(BASE_URL);
        waitForPageLoad();
        
        System.out.println("Current URL: " + driver.getCurrentUrl());
        System.out.println("Page Title: " + driver.getTitle());
        
        // Check for form elements
        try {
            var inputs = driver.findElements(By.tagName("input"));
            System.out.println("Input elements found: " + inputs.size());
            for (int i = 0; i < inputs.size(); i++) {
                WebElement input = inputs.get(i);
                try {
                    String type = input.getDomAttribute("type");
                    String name = input.getDomAttribute("name");
                    String placeholder = input.getDomAttribute("placeholder");
                    String id = input.getDomAttribute("id");
                    System.out.println("  Input[" + i + "]: type=" + type + ", name=" + name + ", placeholder=" + placeholder + ", id=" + id);
                } catch (Exception ex) {
                    System.out.println("  Input[" + i + "]: Could not get attributes");
                }
            }
            
            var buttons = driver.findElements(By.tagName("button"));
            System.out.println("Button elements found: " + buttons.size());
            for (int i = 0; i < buttons.size(); i++) {
                WebElement button = buttons.get(i);
                try {
                    String type = button.getDomAttribute("type");
                    String text = button.getText();
                    String id = button.getDomAttribute("id");
                    System.out.println("  Button[" + i + "]: type=" + type + ", text='" + text + "', id=" + id);
                } catch (Exception ex) {
                    System.out.println("  Button[" + i + "]: Could not get attributes");
                }
            }
            
        } catch (Exception e) {
            System.out.println("⚠ Could not debug form elements: " + e.getMessage());
        }
        
        // This test always passes - it's just for debugging
        Assert.assertTrue(true, "Debug test completed");
    }
    
    @Test(priority = 1, description = "Test successful user login with valid credentials")
    public void testSuccessfulLogin() {
        try {
            System.out.println("=== STARTING LOGIN TEST ===");
            
            // Navigate to login page
            loginPage.navigateToLoginPage(BASE_URL);
            waitForPageLoad();
            
            // Verify we are on login page
            boolean onLoginPage = loginPage.isOnLoginPage();
            System.out.println("On login page: " + onLoginPage);
            Assert.assertTrue(onLoginPage, "Should be on login page");
            
            // Perform login with valid credentials
            // Note: These credentials should exist in your test database
            // You may need to create test users first or use existing ones
            String testUsername = "Vishnuha";
            String testPassword = "11111111";
            
            System.out.println("Attempting login with username: " + testUsername);
            
            loginPage.login(testUsername, testPassword);
            waitForPageLoad();
            
            // Add additional wait for login processing
            System.out.println("Waiting for login to process...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            
            // Verify successful login
            System.out.println("Checking if login was successful...");
            boolean loginSuccessful = loginPage.isLoginSuccessful();
            
            if (!loginSuccessful) {
                // Additional debugging if login failed
                System.out.println("=== LOGIN FAILED - DEBUG INFO ===");
                System.out.println("Current URL: " + driver.getCurrentUrl());
                System.out.println("Page title: " + driver.getTitle());
                
                // Check if we're still on login page with error
                try {
                    String errorMsg = loginPage.getErrorMessage();
                    if (!errorMsg.isEmpty()) {
                        System.out.println("Error message found: " + errorMsg);
                    }
                } catch (Exception e) {
                    System.out.println("No error message found");
                }
                
                // Print a portion of page source for debugging
                String pageSource = driver.getPageSource();
                if (pageSource.length() > 500) {
                    System.out.println("Page source preview: " + pageSource.substring(0, 500));
                } else {
                    System.out.println("Full page source: " + pageSource);
                }
            }
            
            Assert.assertTrue(loginSuccessful, 
                "Login should be successful with valid credentials. Check console output for debugging info.");
            
            System.out.println("✓ Successful login test passed");
            
        } catch (Exception e) {
            System.out.println("⚠ Exception during login test: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }}
    
  /*  @Test(priority = 2, description = "Test login failure with invalid credentials")
    public void testLoginWithInvalidCredentials() {
        // Navigate to login page
        loginPage.navigateToLoginPage(BASE_URL);
        waitForPageLoad();
        
        // Verify we are on login page
        Assert.assertTrue(loginPage.isOnLoginPage(), 
            "Should be on login page");
        
        // Attempt login with invalid credentials
        String invalidUsername = "invaliduser";
        String invalidPassword = "wrongpassword";
        
        loginPage.login(invalidUsername, invalidPassword);
        waitForPageLoad();
        
        // Verify login failed and error message is displayed
        Assert.assertFalse(loginPage.isLoginSuccessful(), 
            "Login should fail with invalid credentials");
        
        // Check for error message (this may vary based on your frontend implementation)
        String errorMessage = loginPage.getErrorMessage();
        Assert.assertFalse(errorMessage.isEmpty(), 
            "Error message should be displayed for invalid credentials");
        Assert.assertTrue(errorMessage.toLowerCase().contains("invalid") || 
                         errorMessage.toLowerCase().contains("incorrect") ||
                         errorMessage.toLowerCase().contains("wrong"),
            "Error message should indicate invalid credentials: " + errorMessage);
        
        System.out.println("✓ Invalid credentials test passed - Error: " + errorMessage);
    }
    
    @Test(priority = 3, description = "Test login with empty username")
    public void testLoginWithEmptyUsername() {
        // Navigate to login page
        loginPage.navigateToLoginPage(BASE_URL);
        waitForPageLoad();
        
        // Attempt login with empty username
        String emptyUsername = "";
        String password = "11111111";
        
        loginPage.login(emptyUsername, password);
        waitForPageLoad();
        
        // Verify login failed
        Assert.assertFalse(loginPage.isLoginSuccessful(), 
            "Login should fail with empty username");
        
        // Verify still on login page or error is shown
        Assert.assertTrue(loginPage.isOnLoginPage() || loginPage.isErrorMessageDisplayed(),
            "Should remain on login page or show error when username is empty");
        
        System.out.println("✓ Empty username test passed");
    }
    
    @Test(priority = 4, description = "Test login with empty password")
    public void testLoginWithEmptyPassword() {
        // Navigate to login page
        loginPage.navigateToLoginPage(BASE_URL);
        waitForPageLoad();
        
        // Attempt login with empty password
        String username = "Vishnuha";
        String emptyPassword = "";
        
        loginPage.login(username, emptyPassword);
        waitForPageLoad();
        
        // Verify login failed
        Assert.assertFalse(loginPage.isLoginSuccessful(), 
            "Login should fail with empty password");
        
        // Verify still on login page or error is shown
        Assert.assertTrue(loginPage.isOnLoginPage() || loginPage.isErrorMessageDisplayed(),
            "Should remain on login page or show error when password is empty");
        
        System.out.println("✓ Empty password test passed");
    }
    
    @Test(priority = 5, description = "Test login form elements are present and interactive")
    public void testLoginFormElements() {
        // Navigate to login page
        loginPage.navigateToLoginPage(BASE_URL);
        waitForPageLoad();
        
        // Test that we can interact with username field
        loginPage.enterUsername("test");
        
        // Test that we can interact with password field
        loginPage.enterPassword("test");
        
        // Clear fields to test clearing functionality
        loginPage.enterUsername("");
        loginPage.enterPassword("");
        
        // If we get here without exceptions, the form elements are interactive
        Assert.assertTrue(true, "Login form elements should be present and interactive");
        
        System.out.println("✓ Login form elements test passed");
    }
    
    /**
     * Helper method to create test user via registration page

    @Test(priority = 0, description = "Ensure test user exists by attempting registration")
    public void ensureTestUserExists() {
        System.out.println("=== ENSURING TEST USER EXISTS ===");
        
        try {
            // Navigate to base URL first
            driver.get(BASE_URL);
            waitForPageLoad();
            
            // Look for registration/signup links
            String[] signupSelectors = {
                "a[href*='register']",
                "a[href*='signup']", 
                "a[href*='sign-up']",
                "button:contains('Sign Up')",
                "button:contains('Register')"
            };
            
            String[] signupLinkTexts = {"Sign Up", "Register", "Create Account", "SIGN UP", "REGISTER"};
            
            boolean foundSignup = false;
            
            // Try to find signup link by text
            for (String linkText : signupLinkTexts) {
                try {
                    WebElement signupLink = driver.findElement(By.linkText(linkText));
                    System.out.println("✓ Found signup link: " + linkText);
                    signupLink.click();
                    waitForPageLoad();
                    foundSignup = true;
                    break;
                } catch (Exception e) {
                    // Try next
                }
            }
            
            if (!foundSignup) {
                // Try direct URL approaches
                String[] signupUrls = {"/register", "/signup", "/sign-up", "/user/register"};
                for (String url : signupUrls) {
                    try {
                        driver.get(BASE_URL + url);
                        waitForPageLoad();
                        
                        // Check if registration form is present
                        driver.findElement(By.cssSelector("input[type='password']"));
                        System.out.println("✓ Found registration page at: " + BASE_URL + url);
                        foundSignup = true;
                        break;
                    } catch (Exception e) {
                        // Continue trying
                    }
                }
            }
            
            if (foundSignup) {
                // Try to register the test user
                System.out.println("Attempting to register test user...");
                
                try {
                    // Fill registration form (adapt selectors based on your form)
                    WebElement usernameField = driver.findElement(By.cssSelector(
                        "input[name='username'], input[placeholder*='username'], input[type='text']:first-of-type"));
                    usernameField.clear();
                    usernameField.sendKeys("Vishnuha");
                    
                    WebElement emailField = driver.findElement(By.cssSelector(
                        "input[name='email'], input[type='email'], input[placeholder*='email']"));
                    emailField.clear();
                    emailField.sendKeys("vishnuha@test.com");
                    
                    WebElement passwordField = driver.findElement(By.cssSelector("input[type='password']"));
                    passwordField.clear();
                    passwordField.sendKeys("11111111");
                    
                    // Submit the form
                    WebElement submitBtn = driver.findElement(By.cssSelector(
                        "button[type='submit'], button:contains('Register'), button:contains('Sign Up')"));
                    submitBtn.click();
                    
                    waitForPageLoad();
                    Thread.sleep(2000);
                    
                    System.out.println("✓ Registration attempt completed");
                    
                } catch (Exception e) {
                    System.out.println("⚠ Could not fill registration form: " + e.getMessage());
                }
            } else {
                System.out.println("⚠ No registration page found - assuming test user already exists");
            }
            
        } catch (Exception e) {
            System.out.println("⚠ Error during user creation: " + e.getMessage());
        }
        
        // This test always passes - it's just trying to ensure user exists
        Assert.assertTrue(true, "Test user creation attempt completed");
    }
} */