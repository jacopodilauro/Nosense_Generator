package com.nonsense.languageweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LanguagewebApplication {

	public static void main(String[] args) {
		System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", "src/main/resources/google-key.json");
		SpringApplication.run(LanguagewebApplication.class, args);
	}
}
