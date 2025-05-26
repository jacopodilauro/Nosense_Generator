package com.nonsense.languageweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Spring Boot application "Languageweb".
 * <p>
 * This class bootstraps and launches the application.
 */
@SpringBootApplication
public class LanguagewebApplication {

	/**
     * The main method that starts the Spring Boot application.
     * <p>
     * It also sets the environment variable for Google Cloud credentials,
     * required to authenticate with services like the Google Cloud Natural Language API.
     *
     * @param args command-line arguments passed to the application
     */
	public static void main(String[] args) {
		/** Set the path to the Google Cloud service account key file. */
		System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", "src/main/resources/google-key.json");

		/** Launch the Spring Boot application. */
		SpringApplication.run(LanguagewebApplication.class, args);
	}
}
