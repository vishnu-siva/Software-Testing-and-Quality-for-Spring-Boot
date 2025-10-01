package com.fiteasy.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

public class SignupPage {
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    // Page URL
    private static final String SIGNUP_URL = "/signup";
    
    // Locators using @FindBy annotations
    @FindBy(id = "username")
    private WebElement usernameField;
    
    @FindBy(id = "email")
    private WebElement emailField;
    
    @FindBy(id = "password")
    private WebElement passwordField;
    
    @FindBy(id = "confirmPassword")
    private WebElement confirmPasswordField;
    
    @FindBy(css = "button[type='submit']")
    private WebElement signupButton;
    
    @FindBy(css = ".error-message")
    private WebElement errorMessage;
    
    @FindBy(css = ".success-message")
    private WebElement successMessage;
    
    @FindBy(linkText = "Login")
    private WebElement loginLink;
    
    @FindBy(css = "h1, h2")
    private WebElement pageTitle;
    
    // Alternative locators for common input patterns
    @FindBy(css = "input[name='username'], input[placeholder*='username'], input[placeholder*='Username']")
    private WebElement usernameFieldAlt;
    
    @FindBy(css = "input[name='email'], input[placeholder*='email'], input[placeholder*='Email'], input[type='email']")
    private WebElement emailFieldAlt;
    
    @FindBy(css = "input[name='password'], input[placeholder*='password'], input[placeholder*='Password']")
    private WebElement passwordFieldAlt;
    
    @FindBy(css = "input[name='confirmPassword'], input[placeholder*='confirm'], input[placeholder*='Confirm']")
    private WebElement confirmPasswordFieldAlt;
    
    @FindBy(css = "button:contains('Sign Up'), button:contains('Register'), button:contains('Create Account'), input[type='submit']")
    private WebElement signupButtonAlt;
    
    // Terms and Conditions checkbox
    @FindBy(css = "input[type='checkbox']")
    private WebElement termsCheckbox;
    
    public SignupPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Navigate to signup page
     */
    public void navigateToSignupPage(String baseUrl) {
        System.out.println("Navigating to signup page...");
        
        // Try multiple possible signup URLs
        String[] signupUrls = {
            SIGNUP_URL,
            "/register",
            "/sign-up",
            "/registration",
            "/create-account",
            "/auth/signup",
            "/user/signup"
        };
        
        boolean signupPageFound = false;
        
        for (String url : signupUrls) {
            try {
                String fullUrl = baseUrl + url;
                System.out.println("Trying URL: " + fullUrl);
                driver.get(fullUrl);
                Thread.sleep(1000);
                
                // Check if this looks like a signup page
                if (driver.getCurrentUrl().contains("signup") || driver.getCurrentUrl().contains("register") ||
                    driver.getTitle().toLowerCase().contains("signup") ||
                    driver.getTitle().toLowerCase().contains("register") ||
                    driver.getTitle().toLowerCase().contains("sign up")) {
                    
                    System.out.println("✓ Found signup page at: " + driver.getCurrentUrl());
                    signupPageFound = true;
                    break;
                }
                
                // Check for signup form elements
                try {
                    driver.findElement(By.cssSelector("input[type='email'], input[name='email']"));
                    driver.findElement(By.cssSelector("input[type='password'], input[name='password']"));
                    System.out.println("✓ Found signup form at: " + driver.getCurrentUrl());
                    signupPageFound = true;
                    break;
                } catch (Exception e) {
                    // Continue trying
                }
                
            } catch (Exception e) {
                System.out.println("⚠ Could not access: " + baseUrl + url);
            }
        }
        
        if (!signupPageFound) {
            // Fallback to base URL and look for signup links
            System.out.println("⚠ No direct signup page found, trying base URL");
            driver.get(baseUrl);
            
            // Look for signup links
            String[] signupLinkTexts = {"Sign Up", "Register", "Sign up", "Create Account", "Join"};
            for (String linkText : signupLinkTexts) {
                try {
                    WebElement signupLink = driver.findElement(By.linkText(linkText));
                    System.out.println("✓ Found signup link: " + linkText);
                    signupLink.click();
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
        System.out.println("✓ Entered username: " + username);
    }
    
    /**
     * Enter email
     */
    public void enterEmail(String email) {
        WebElement emailElement = findEmailField();
        wait.until(ExpectedConditions.elementToBeClickable(emailElement));
        emailElement.clear();
        emailElement.sendKeys(email);
        System.out.println("✓ Entered email: " + email);
    }
    
    /**
     * Enter password
     */
    public void enterPassword(String password) {
        WebElement passwordElement = findPasswordField();
        wait.until(ExpectedConditions.elementToBeClickable(passwordElement));
        passwordElement.clear();
        passwordElement.sendKeys(password);
        System.out.println("✓ Entered password: " + password);
    }
    
    /**
     * Enter confirm password
     */
    public void enterConfirmPassword(String confirmPassword) {
        WebElement confirmPasswordElement = findConfirmPasswordField();
        if (confirmPasswordElement != null) {
            wait.until(ExpectedConditions.elementToBeClickable(confirmPasswordElement));
            confirmPasswordElement.clear();
            confirmPasswordElement.sendKeys(confirmPassword);
            System.out.println("✓ Entered confirm password: " + confirmPassword);
        } else {
            System.out.println("⚠ Confirm password field not found - may not be required");
        }
    }
    
    /**
     * Accept Terms and Conditions
     */
    public void acceptTermsAndConditions() {
        try {
            // Try multiple selectors for terms and conditions checkbox
            String[] checkboxSelectors = {
                "input[type='checkbox']",
                "input[name*='terms']",
                "input[name*='Terms']",
                "input[name*='condition']",
                "input[name*='Condition']",
                "input[id*='terms']",
                "input[id*='Terms']",
                "input[id*='condition']",
                "input[id*='Condition']"
            };
            
            WebElement checkbox = null;
            for (String selector : checkboxSelectors) {
                try {
                    List<WebElement> checkboxes = driver.findElements(By.cssSelector(selector));
                    for (WebElement cb : checkboxes) {
                        // Check if this checkbox is related to terms and conditions
                        String parentText = cb.findElement(By.xpath("./parent::*")).getText().toLowerCase();
                        if (parentText.contains("terms") || parentText.contains("condition") || 
                            parentText.contains("accept") || parentText.contains("agree")) {
                            checkbox = cb;
                            break;
                        }
                    }
                    if (checkbox != null) break;
                } catch (Exception e) {
                    // Continue trying
                }
            }
            
            if (checkbox != null && !checkbox.isSelected()) {
                wait.until(ExpectedConditions.elementToBeClickable(checkbox));
                checkbox.click();
                System.out.println("✓ Accepted Terms and Conditions");
            } else if (checkbox != null && checkbox.isSelected()) {
                System.out.println("✓ Terms and Conditions already accepted");
            } else {
                System.out.println("⚠ Terms and Conditions checkbox not found - may not be required");
            }
        } catch (Exception e) {
            System.out.println("⚠ Could not find or click Terms and Conditions checkbox: " + e.getMessage());
        }
    }
    
    /**
     * Click signup button
     */
    public void clickSignupButton() {
        // First try to accept terms and conditions if checkbox exists
        acceptTermsAndConditions();
        
        WebElement signupElement = findSignupButton();
        wait.until(ExpectedConditions.elementToBeClickable(signupElement));
        signupElement.click();
        System.out.println("✓ Clicked signup button");
    }
    
    /**
     * Perform complete signup
     */
    public void signup(String username, String email, String password) {
        System.out.println("Starting signup process...");
        System.out.println("Username: " + username);
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        System.out.println("Current URL before signup: " + driver.getCurrentUrl());
        
        enterUsername(username);
        enterEmail(email);
        enterPassword(password);
        clickSignupButton();
        
        System.out.println("Signup form submitted");
    }
    
    /**
     * Perform complete signup with confirm password
     */
    public void signupWithConfirmation(String username, String email, String password, String confirmPassword) {
        System.out.println("Starting signup process with password confirmation...");
        System.out.println("Username: " + username);
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        System.out.println("Confirm Password: " + confirmPassword);
        System.out.println("Current URL before signup: " + driver.getCurrentUrl());
        
        enterUsername(username);
        enterEmail(email);
        enterPassword(password);
        enterConfirmPassword(confirmPassword);
        clickSignupButton();
        
        System.out.println("Signup form with confirmation submitted");
    }
    
    /**
     * Check if signup was successful by looking for redirect to sign-in prompt
     */
    public boolean isSignupSuccessful() {
        System.out.println("Checking if signup was successful...");
        
        try {
            // Wait a moment for page to respond
            Thread.sleep(3000);
            
            String currentUrl = driver.getCurrentUrl();
            System.out.println("Current URL after signup: " + currentUrl);
            System.out.println("Page title after signup: " + driver.getTitle());
            
            // Primary success indicator: redirect to sign-in/login page
            boolean redirectedToSignIn = currentUrl.contains("/login") || 
                                        currentUrl.contains("/signin") ||
                                        currentUrl.contains("/sign-in");
            
            if (redirectedToSignIn) {
                System.out.println("✓ Successfully redirected to sign-in page");
                
                // Additional verification: look for sign-in prompt elements
                try {
                    // Check for login form elements
                    boolean hasLoginForm = driver.findElements(By.cssSelector("input[type='password']")).size() > 0;
                    
                    // Check for username/email input field
                    boolean hasUsernameField = driver.findElements(By.cssSelector("input[name='username'], input[name='email'], input[type='text']")).size() > 0;
                    
                    // Check for sign-in related text
                    String pageSource = driver.getPageSource().toLowerCase();
                    boolean hasSignInText = pageSource.contains("sign in") || 
                                          pageSource.contains("login") ||
                                          pageSource.contains("please sign in") ||
                                          pageSource.contains("welcome back");
                    
                    System.out.println("Has login form (password field): " + hasLoginForm);
                    System.out.println("Has username/email field: " + hasUsernameField);
                    System.out.println("Has sign-in text: " + hasSignInText);
                    
                    return hasLoginForm && hasUsernameField; // Both fields required for sign-in form
                    
                } catch (Exception e) {
                    System.out.println("✓ On sign-in page but couldn't verify form elements");
                    return true; // URL redirect is primary indicator
                }
            }
            
            // Check for success message with sign-in prompt on same page
            try {
                WebElement success = driver.findElement(By.cssSelector(".success-message, .alert-success, [class*='success'], .message"));
                if (success.isDisplayed()) {
                    String successText = success.getText().toLowerCase();
                    boolean hasSignInPrompt = successText.contains("sign in") || 
                                            successText.contains("login") ||
                                            successText.contains("please sign in") ||
                                            successText.contains("registration successful");
                    
                    System.out.println("Found success message: " + success.getText());
                    if (hasSignInPrompt) {
                        System.out.println("✓ Success message contains sign-in prompt");
                        return true;
                    }
                }
            } catch (Exception e) {
                // Continue checking other indicators
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
            
            // Check if form has disappeared (might indicate successful submission)
            if (currentUrl.contains("/signup") || currentUrl.contains("/register")) {
                try {
                    List<WebElement> emailFields = driver.findElements(By.cssSelector("input[type='email'], input[name='email']"));
                    List<WebElement> passwordFields = driver.findElements(By.cssSelector("input[type='password'], input[name='password']"));
                    
                    boolean formStillPresent = emailFields.size() > 0 && passwordFields.size() > 0;
                    
                    if (!formStillPresent) {
                        System.out.println("✓ Signup form has disappeared - likely successful");
                        // Check if there's any text indicating to sign in
                        String pageText = driver.getPageSource().toLowerCase();
                        if (pageText.contains("sign in") || pageText.contains("login")) {
                            System.out.println("✓ Found sign-in prompt text on page");
                            return true;
                        }
                    } else {
                        System.out.println("⚠ Still on signup page with form present - registration may not have processed");
                        return false;
                    }
                } catch (Exception e) {
                    System.out.println("Error checking form presence: " + e.getMessage());
                }
            }
            
            System.out.println("ℹ Registration submitted but no clear sign-in redirect/prompt detected");
            return false;
            
        } catch (Exception e) {
            System.out.println("⚠ Error checking signup success: " + e.getMessage());
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
     * Get success message text
     */
    public String getSuccessMessage() {
        try {
            WebElement success = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".success-message, .alert-success, [class*='success']")));
            return success.getText();
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        try {
            WebElement error = driver.findElement(By.cssSelector(".error-message, .alert-danger, .error, [class*='error']"));
            return error.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if success message is displayed
     */
    public boolean isSuccessMessageDisplayed() {
        try {
            WebElement success = driver.findElement(By.cssSelector(".success-message, .alert-success, [class*='success']"));
            return success.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if we are on the signup page
     */
    public boolean isOnSignupPage() {
        String currentUrl = driver.getCurrentUrl();
        return currentUrl.contains("/signup") || 
               currentUrl.contains("/register") ||
               currentUrl.contains("signup") ||
               driver.getTitle().toLowerCase().contains("signup") ||
               driver.getTitle().toLowerCase().contains("register");
    }
    
    /**
     * Check for validation errors
     */
    public boolean hasValidationErrors() {
        try {
            // Look for various validation error indicators
            String[] validationSelectors = {
                ".validation-error",
                ".field-error",
                ".invalid-feedback",
                "[class*='validation']",
                ".form-error",
                ".input-error",
                "[class*='invalid']"
            };
            
            for (String selector : validationSelectors) {
                try {
                    WebElement validationError = driver.findElement(By.cssSelector(selector));
                    if (validationError.isDisplayed()) {
                        System.out.println("✓ Found validation error: " + validationError.getText());
                        return true;
                    }
                } catch (Exception e) {
                    // Continue checking other selectors
                }
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get validation error messages
     */
    public String getValidationErrors() {
        try {
            StringBuilder errors = new StringBuilder();
            String[] validationSelectors = {
                ".validation-error",
                ".field-error", 
                ".invalid-feedback",
                "[class*='validation']"
            };
            
            for (String selector : validationSelectors) {
                try {
                    WebElement error = driver.findElement(By.cssSelector(selector));
                    if (error.isDisplayed()) {
                        errors.append(error.getText()).append("; ");
                    }
                } catch (Exception e) {
                    // Continue
                }
            }
            
            return errors.toString();
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Click login link (if user wants to go to login instead)
     */
    public void clickLoginLink() {
        try {
            String[] loginLinkTexts = {"Login", "Sign In", "Already have an account?", "Sign in"};
            
            for (String linkText : loginLinkTexts) {
                try {
                    WebElement loginLinkElement = driver.findElement(By.linkText(linkText));
                    loginLinkElement.click();
                    System.out.println("✓ Clicked login link: " + linkText);
                    Thread.sleep(1000);
                    return;
                } catch (Exception e) {
                    // Try next link text
                }
            }
            
            System.out.println("⚠ Could not find login link");
            
        } catch (Exception e) {
            System.out.println("⚠ Error clicking login link: " + e.getMessage());
        }
    }
    
    // Helper methods to find elements with fallback options
    private WebElement findUsernameField() {
        try {
            if (usernameField.isDisplayed()) {
                return usernameField;
            }
        } catch (Exception e) {
            // Try alternative
        }
        
        try {
            if (usernameFieldAlt.isDisplayed()) {
                return usernameFieldAlt;
            }
        } catch (Exception e) {
            // Try generic approach
        }
        
        // Last resort - try common selectors
        String[] selectors = {
            "input[name='username']",
            "#username", 
            "input[placeholder*='username' i]",
            "input[type='text']:first-of-type",
            "input:first-of-type"
        };
        
        for (String selector : selectors) {
            try {
                WebElement element = driver.findElement(By.cssSelector(selector));
                if (element.isDisplayed()) {
                    return element;
                }
            } catch (Exception e) {
                // Continue to next selector
            }
        }
        
        throw new RuntimeException("Could not find username field");
    }
    
    private WebElement findEmailField() {
        try {
            if (emailField.isDisplayed()) {
                return emailField;
            }
        } catch (Exception e) {
            // Try alternative
        }
        
        try {
            if (emailFieldAlt.isDisplayed()) {
                return emailFieldAlt;
            }
        } catch (Exception e) {
            // Try generic approach
        }
        
        // Last resort - try common selectors
        String[] selectors = {
            "input[type='email']",
            "input[name='email']", 
            "#email",
            "input[placeholder*='email' i]"
        };
        
        for (String selector : selectors) {
            try {
                WebElement element = driver.findElement(By.cssSelector(selector));
                if (element.isDisplayed()) {
                    return element;
                }
            } catch (Exception e) {
                // Continue to next selector
            }
        }
        
        throw new RuntimeException("Could not find email field");
    }
    
    private WebElement findPasswordField() {
        try {
            if (passwordField.isDisplayed()) {
                return passwordField;
            }
        } catch (Exception e) {
            // Try alternative
        }
        
        try {
            if (passwordFieldAlt.isDisplayed()) {
                return passwordFieldAlt;
            }
        } catch (Exception e) {
            // Try generic approach
        }
        
        // Find first password field
        return driver.findElement(By.cssSelector("input[type='password']:first-of-type"));
    }
    
    private WebElement findConfirmPasswordField() {
        try {
            if (confirmPasswordField.isDisplayed()) {
                return confirmPasswordField;
            }
        } catch (Exception e) {
            // Try alternative
        }
        
        try {
            if (confirmPasswordFieldAlt.isDisplayed()) {
                return confirmPasswordFieldAlt;
            }
        } catch (Exception e) {
            // Try generic approach
        }
        
        // Try to find second password field or confirm password field
        try {
            return driver.findElement(By.cssSelector("input[type='password']:nth-of-type(2)"));
        } catch (Exception e) {
            // Confirm password field might not exist
            return null;
        }
    }
    
    private WebElement findSignupButton() {
        try {
            if (signupButton.isDisplayed()) {
                return signupButton;
            }
        } catch (Exception e) {
            // Try alternative
        }
        
        try {
            if (signupButtonAlt.isDisplayed()) {
                return signupButtonAlt;
            }
        } catch (Exception e) {
            // Try generic approach
        }
        
        // Last resort - try common selectors
        String[] selectors = {
            "button[type='submit']",
            "input[type='submit']",
            "button",
            "input[type='button']"
        };
        
        for (String selector : selectors) {
            try {
                WebElement element = driver.findElement(By.cssSelector(selector));
                if (element.isDisplayed()) {
                    return element;
                }
            } catch (Exception e) {
                // Continue to next selector
            }
        }
        
        return signupButton; // Fallback to original element
    }
}