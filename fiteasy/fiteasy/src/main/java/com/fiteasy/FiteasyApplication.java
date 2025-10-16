package com.fiteasy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class FiteasyApplication {

	private static final Logger log = LoggerFactory.getLogger(FiteasyApplication.class);

	private final Environment env;

	public FiteasyApplication(Environment env) {
		this.env = env;
	}

	public static void main(String[] args) {
		SpringApplication.run(FiteasyApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void logApplicationUrls() {
		String port = env.getProperty("server.port", "8080");
		String frontendUrl = env.getProperty("app.cors.allowed-origins", "http://localhost:3000");
		log.info("FitEasy Application Started Successfully!");
		log.info("Frontend URL: {}", frontendUrl);
		log.info("Backend URL:  http://localhost:{}", port);
		log.info("API Base URL: http://localhost:{}/api", port);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				String allowedOrigin = env.getProperty("app.cors.allowed-origins");
				registry.addMapping("/api/**")
						.allowedOrigins(allowedOrigin)
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
						.allowedHeaders("Authorization", "Content-Type", "Accept")
						.allowCredentials(true);
			}
		};
	}
}