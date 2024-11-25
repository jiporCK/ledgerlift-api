package com.example.ledgerlift;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class LedgerliftApplication {

	public static void main(String[] args) {
		SpringApplication.run(LedgerliftApplication.class, args);
	}

}
