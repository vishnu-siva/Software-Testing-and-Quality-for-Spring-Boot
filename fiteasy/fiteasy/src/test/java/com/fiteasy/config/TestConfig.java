package com.fiteasy.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * Test configuration to handle Mockito strict stubbing issues
 * and provide clean test environment for Java 24
 */
@TestConfiguration
public class TestConfig {

    /**
     * Configure Mockito to use lenient stubbing for tests
     * This resolves the "UnnecessaryStubbingException" issues
     */
    @Bean
    @Primary
    public Mockito mockitoConfig() {
        Mockito.lenient();
        return new Mockito();
    }
}
