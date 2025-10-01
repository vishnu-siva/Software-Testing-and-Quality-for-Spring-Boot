package com.fiteasy.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage {
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    // Page URL
    private static final String LOGIN_URL = "/login";
    
    // Locators using @FindBy annotations
    @FindBy(id = "username")
    private WebElement usernameField;
    
    @FindBy(id = "password")
    private WebElement passwordField;
    
    @FindBy(css = "button[type='submit']")
    private WebElement loginButton;
    
    @FindBy(css = ".error-message")
    private WebElement errorMessage;
    
    @FindBy(css = ".success-message")
    private WebElement successMessage;
    
    @FindBy(linkText = "Sign Up")
    private WebElement signUpLink;
    
    @FindBy(css = "h1, h2")
    private WebElement pageTitle;
    
    // Alternative locators for common input patterns
    @FindBy(css = "input[name='username'], input[placeholder*='username'], input[placeholder*='Username']")
    private WebElement usernameFieldAlt;
    
    @FindBy(css = "input[name='password'], input[placeholder*='password'], input[placeholder*='Password']")
    private WebElement passwordFieldAlt;
    
    @FindBy(css = "button:contains('Login'), button:contains('Sign In'), input[type='submit']")
    private WebElement loginButtonAlt;
    
    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Navigate to login page
     */
    public void navigateToLoginPage(String baseUrl) {
        System.out.println("Navigating to login page...");
        
        // Try multiple possible login URLs
        String[] loginUrls = {
            LOGIN_URL,
            "/signin",
            "/sign-in",
            "/auth/login",
            "/user/login"
        };
        
        boolean loginPageFound = false;
        
        for (String url : loginUrls) {
            try {
                String fullUrl = baseUrl + url;
                System.out.println("Trying URL: " + fullUrl);
                driver.get(fullUrl);
                Thread.sleep(1000);
                
                // Check if this looks like a login page
                if (driver.getCurrentUrl().contains("login") || driver.getCurrentUrl().contains("signin") ||
                    driver.getTitle().toLowerCase().contains("login") ||
                    driver.getTitle().toLowerCase().contains("sign in")) {
                    
                    System.out.println("✓ Found login page at: " + driver.getCurrentUrl());
                    loginPageFound = true;
                    break;
                }
                
                // Check for login form elements
                try {
                    driver.findElement(By.cssSelector("input[type='password']"));
                    System.out.println("✓ Found login form at: " + driver.getCurrentUrl());
                    loginPageFound = true;
                    break;
                } catch (Exception e) {
                    // Continue trying
                }
                
            } catch (Exception e) {
                System.out.println("⚠ Could not access: " + baseUrl + url);
            }
        }
        
        if (!loginPageFound) {
            // Fallback to base URL and look for login links
            System.out.println("⚠ No direct login page found, trying base URL");
            driver.get(baseUrl);
            
            // Look for login links
            String[] loginLinkTexts = {"Login", "Sign In", "Sign in", "LOG IN"};
            for (String linkText : loginLinkTexts) {
                try {
                    WebElement loginLink = driver.findElement(By.linkText(linkText));
                    System.out.println("✓ Found login link: " + linkText);
                    loginLink.click();
                    Thread.sleep(1000);
                    break;
                } catch (Exception e) {
                    // Try next link text
                }
            }
        }
        
        System.out.println("Final URL: " + driver.getCurrentUrl());
        System.out.println("Page title: " + driver.getTitle());
    }
    
    /**
     * Enter username
     */
    public void enterUsername(String username) {
        WebElement usernameElement = findUsernameField();
        wait.until(ExpectedConditions.elementToBeClickable(usernameElement));
        usernameElement.clear();
        usernameElement.sendKeys(username);
    }
    
    /**
     * Enter password
     */
    public void enterPassword(String password) {
        WebElement passwordElement = findPasswordField();
        wait.until(ExpectedConditions.elementToBeClickable(passwordElement));
        passwordElement.clear();
        passwordElement.sendKeys(password);
    }
    
    /**
     * Click login button
     */
    public void clickLoginButton() {
        WebElement loginElement = findLoginButton();
        wait.until(ExpectedConditions.elementToBeClickable(loginElement));
        loginElement.click();
    }
    
    /**
     * Perform complete login
     */
    public void login(String username, String password) {
        System.out.println("Starting login process...");
        System.out.println("Username: " + username);
        System.out.println("Password: [MASKED]");
        System.out.println("Current URL before login: " + driver.getCurrentUrl());
        
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        
        System.out.println("Login form submitted");
    }
    
    /**
     * Check if login was successful by looking for success indicators
     */
    public boolean isLoginSuccessful() {
        System.out.println("Checking if login was successful...");
        
        try {
            // Wait a moment for page to respond
            Thread.sleep(2000);
            
            String currentUrl = driver.getCurrentUrl();
            System.out.println("Current URL after login: " + currentUrl);
            System.out.println("Page title after login: " + driver.getTitle());
            
            // Check for success message first
            try {
                WebElement success = driver.findElement(By.cssSelector(".success-message, .alert-success, [class*='success']"));
                if (success.isDisplayed()) {
                    System.out.println("✓ Found success message: " + success.getText());
                    return true;
                }
            } catch (Exception e) {
                System.out.println("⚠ No success message found");
            }
            
            // Check for error messages
            try {
                WebElement error = driver.findElement(By.cssSelector(".error-message, .alert-danger, .error, [class*='error']"));
                if (error.isDisplayed()) {
                    System.out.println("⚠ Found error message: " + error.getText());
                    return false;
                }
            } catch (Exception e) {
                System.out.println("✓ No error message found");
            }
            
            // Check for URL change (redirect to dashboard or another page)
            boolean urlChanged = currentUrl.contains("/dashboard") || 
                               currentUrl.contains("/home") || 
                               currentUrl.contains("/profile") ||
                               currentUrl.contains("/user") ||
                               !currentUrl.contains("/login");
            
            System.out.println("URL indicates success: " + urlChanged);
            
            if (!urlChanged) {
                // Still on login page, check if form elements are still there
                try {
                    driver.findElement(By.cssSelector("input[type='password']"));
                    System.out.println("⚠ Still on login page (password field found)");
                    return false;
                } catch (Exception e) {
                    System.out.println("✓ Password field not found - might have left login page");
                    return true;
                }
            }
            
            return urlChanged;
            
        } catch (Exception e) {
            System.out.println("⚠ Error checking login success: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get error message text
     */
    public String getErrorMessage() {
        try {
            WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".error-message, .alert-danger, .error, [class*='error']")));
            return error.getText();
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        try {
            WebElement error = driver.findElement(By.cssSelector(".error-message, .alert-danger, .error"));
            return error.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if we are on the login page
     */
    public boolean isOnLoginPage() {
        String currentUrl = driver.getCurrentUrl();
        return currentUrl.contains("/login") || 
               currentUrl.contains("login") ||
               driver.getTitle().toLowerCase().contains("login");
    }
    
    // Helper methods to find elements with fallback options
    private WebElement findUsernameField() {
        try {
            return usernameField.isDisplayed() ? usernameField : usernameFieldAlt;
        } catch (Exception e) {
            return driver.findElement(By.cssSelector("input[type='text']:first-of-type, input:first-of-type"));
        }
    }
    
    private WebElement findPasswordField() {
        try {
            return passwordField.isDisplayed() ? passwordField : passwordFieldAlt;
        } catch (Exception e) {
            return driver.findElement(By.cssSelector("input[type='password']"));
        }
    }
    
    private WebElement findLoginButton() {
        try {
            return loginButton.isDisplayed() ? loginButton : loginButtonAlt;
        } catch (Exception e) {
            return driver.findElement(By.cssSelector("button, input[type='submit']"));
        }
    }
}