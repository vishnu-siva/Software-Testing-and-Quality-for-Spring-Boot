package com.fiteasy.selenium.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WebDriverConfig {
    
    private static final int DEFAULT_TIMEOUT = 10;
    
    public static WebDriver createChromeDriver() {
        // Selenium 4+ automatically manages ChromeDriver
        // No need for external WebDriverManager
        
        ChromeOptions options = new ChromeOptions();
        
        // Basic stability options
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-extensions");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-blink-features=AutomationControlled");
        
        // Additional stability options for Chrome 140+
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-background-timer-throttling");
        options.addArguments("--disable-backgrounding-occluded-windows");
        options.addArguments("--disable-renderer-backgrounding");
        options.addArguments("--disable-features=TranslateUI,BlinkGenPropertyTrees");
        options.addArguments("--disable-ipc-flooding-protection");
        options.addArguments("--disable-component-update");
        options.addArguments("--disable-default-apps");
        options.addArguments("--disable-domain-reliability");
        options.addArguments("--disable-background-networking");
        options.addArguments("--disable-sync");
        options.addArguments("--disable-translate");
        options.addArguments("--disable-web-security");
        options.addArguments("--disable-features=VizDisplayCompositor");
        options.addArguments("--force-color-profile=srgb");
        options.addArguments("--metrics-recording-only");
        options.addArguments("--use-mock-keychain");
        options.addArguments("--window-size=1920,1080");
        
        // Avoid window maximize timeout
        options.addArguments("--start-maximized");
        
        // Add unique user-data-dir to avoid session conflicts in CI/CD
        String userDataDir = "/tmp/chrome-user-data-" + System.currentTimeMillis();
        options.addArguments("--user-data-dir=" + userDataDir);
        
        // Disable Chrome DevTools Protocol to avoid version warnings
        options.addArguments("--disable-dev-tools");
        options.setExperimentalOption("useAutomationExtension", false);
        
        // Enable headless mode for CI/CD environments
        if (isHeadlessMode()) {
            options.addArguments("--headless=new");
        }
        
        // Configure Chrome service with longer timeout
        ChromeDriverService service = new ChromeDriverService.Builder()
            .withTimeout(java.time.Duration.ofMinutes(2))
            .build();
        
        return new ChromeDriver(service, options);
    }
    
    public static WebDriver createFirefoxDriver() {
        // Selenium 4+ automatically manages FirefoxDriver
        
        FirefoxOptions options = new FirefoxOptions();
        // For headless mode (uncomment if needed for CI/CD)
        // options.addArguments("--headless");
        
        return new FirefoxDriver(options);
    }
    
    public static WebDriverWait createWebDriverWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }
    
    public static WebDriverWait createWebDriverWait(WebDriver driver, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }
    
    private static boolean isHeadlessMode() {
        return System.getProperty("selenium.headless", "false").equalsIgnoreCase("true") ||
               System.getenv("CI") != null;
    }
}