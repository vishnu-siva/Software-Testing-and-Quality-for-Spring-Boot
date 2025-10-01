package com.fiteasy.config;

import com.fiteasy.FiteasyApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Cucumber Spring Configuration
 * Enables Spring Boot context for Cucumber BDD tests
 */
@CucumberContextConfiguration
@SpringBootTest(classes = FiteasyApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CucumberConfig {
    // This class provides Spring Boot context for Cucumber tests
}
