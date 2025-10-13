package com.fiteasy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for REST API
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/users/register", "/api/users/login", "/api/users/logout").permitAll()
                .requestMatchers("/api/users/check-username/**", "/api/users/check-email/**").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> {}); // Enable HTTP Basic authentication for testing

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Create test users for testing purposes
        UserDetails user1 = User.builder()
            .username("user1")
            .password(passwordEncoder().encode("password1"))
            .roles("USER")
            .build();

        UserDetails user2 = User.builder()
            .username("user2")
            .password(passwordEncoder().encode("password2"))
            .roles("USER")
            .build();

        // Add test user for Selenium tests
        UserDetails testUser = User.builder()
            .username("Vishnuha")
            .password(passwordEncoder().encode("11111111"))
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(user1, user2, testUser);
    }
}