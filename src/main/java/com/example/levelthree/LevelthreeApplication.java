package com.example.levelthree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LevelthreeApplication {

	public static void main(String[] args) {
		SpringApplication.run(LevelthreeApplication.class, args);
	}

}
