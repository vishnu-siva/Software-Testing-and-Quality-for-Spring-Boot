package com.fiteasy.selenium.base;

import com.fiteasy.selenium.config.WebDriverConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseSeleniumTest {
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    
    // Base URLs for the application
    protected static final String BASE_URL = "http://localhost:3000";
    protected static final String API_BASE_URL = "http://localhost:8080/api";
    
    @BeforeMethod
    public void setUp() {
        try {
            // Initialize WebDriver (Chrome by default)
            driver = WebDriverConfig.createChromeDriver();
            wait = WebDriverConfig.createWebDriverWait(driver);
            
            // Chrome is already started maximized via --start-maximized flag
            // No need to call maximize() which can cause timeouts
            
            // Set timeouts
            driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(java.time.Duration.ofSeconds(30));
            driver.manage().timeouts().scriptTimeout(java.time.Duration.ofSeconds(30));
            
        } catch (Exception e) {
            System.out.println("⚠ Failed to initialize WebDriver: " + e.getMessage());
            throw e;
        }
    }
    
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (WebDriverException ignored) {
                // session may already be gone
            }
        }
    }
    
    /**
     * Navigate to the application base URL
     */
    protected void navigateToHomePage() {
        if (!isDriverSessionActive()) return;
        driver.get(BASE_URL);
    }
    
    /**
     * Wait for page to load (basic implementation)
     */
    protected void waitForPageLoad() {
        try {
            Thread.sleep(1000); // Basic wait - can be improved with better conditions
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Returns true if driver session is still active.
     */
    protected boolean isDriverSessionActive() {
        try {
            if (driver == null) return false;
            driver.getWindowHandles();
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    /**
     * Safely get current URL without throwing if session is gone.
     */
    protected String safeGetCurrentUrl() {
        try {
            return driver != null ? driver.getCurrentUrl() : "<driver null>";
        } catch (WebDriverException e) {
            return "<session not available>";
        }
    }

    /**
     * Safely get page title without throwing if session is gone.
     */
    protected String safeGetTitle() {
        try {
            return driver != null ? driver.getTitle() : "<driver null>";
        } catch (WebDriverException e) {
            return "<session not available>";
        }
    }

    /**
     * Safely get a page source snippet without throwing if session is gone.
     */
    protected String safeGetPageSourceSnippet(int maxLen) {
        try {
            if (driver == null) return "<driver null>";
            String src = driver.getPageSource();
            return src.substring(0, Math.min(maxLen, src.length()));
        } catch (WebDriverException e) {
            return "<session not available>";
        }
    }
}
