package com.fiteasy.selenium.tests;

import com.fiteasy.selenium.base.BaseSeleniumTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SeleniumSetupVerificationTest extends BaseSeleniumTest {
    
    @Test(priority = 1, description = "Verify that WebDriver setup works correctly")
    public void testWebDriverSetup() {
        // Navigate to a basic page to verify WebDriver is working
        driver.get("https://www.google.com");
        
        // Verify we can get the page title
        String title = driver.getTitle();
        Assert.assertNotNull(title, "Page title should not be null");
        Assert.assertTrue(title.toLowerCase().contains("google"), 
            "Page title should contain 'google', but was: " + title);
        
        System.out.println("✓ WebDriver setup verification passed - Title: " + title);
    }
    
    @Test(priority = 2, description = "Verify that base URL navigation works (if application is running)")
    public void testBaseURLNavigation() {
        try {
            // Try to navigate to the application base URL
            navigateToHomePage();
            
            // Just verify we can access the URL without errors
            String currentUrl = driver.getCurrentUrl();
            Assert.assertEquals(currentUrl, BASE_URL + "/" , 
                "Should navigate to base URL successfully");
                
            System.out.println("✓ Base URL navigation test passed - URL: " + currentUrl);
            
        } catch (Exception e) {
            System.out.println("⚠ Base URL navigation test skipped - Application may not be running");
            System.out.println("  Error: " + e.getMessage());
            System.out.println("  Ensure both frontend (port 3000) and backend (port 8080) are running");
            
            // Skip this test if application is not running
            throw new org.testng.SkipException("Application is not running on " + BASE_URL);
        }
    }
}