package com.coffee.americanote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AmericanoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmericanoApplication.class, args);
	}
}
