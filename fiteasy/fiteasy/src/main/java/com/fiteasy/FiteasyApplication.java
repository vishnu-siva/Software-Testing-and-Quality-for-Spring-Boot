package com.fiteasy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class FiteasyApplication {

	public static void main(String[] args) {
		System.out.println("Starting FitEasy Fitness Application...");
		SpringApplication.run(FiteasyApplication.class, args);
		System.out.println("FitEasy Application Started Successfully!");
		System.out.println("Frontend URL: http://localhost:3000");
		System.out.println("Backend URL: http://localhost:8080");
		System.out.println("API Base URL: http://localhost:8080/api");
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**")
						.allowedOrigins("http://localhost:3000")
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
						.allowedHeaders("*")
						.allowCredentials(true);
			}
		};
	}
}