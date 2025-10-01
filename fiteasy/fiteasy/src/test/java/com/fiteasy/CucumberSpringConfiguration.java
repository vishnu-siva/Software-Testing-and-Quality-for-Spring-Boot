package com.fiteasy;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Cucumber Spring Configuration
 * This class provides the Spring Boot context for Cucumber BDD tests
 */
@CucumberContextConfiguration
@SpringBootTest(classes = FiteasyApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberSpringConfiguration {
    // This class serves as the Spring configuration for Cucumber tests
}
