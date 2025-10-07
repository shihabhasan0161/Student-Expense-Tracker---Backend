package com.studentexpensetracker.studentexpensetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class StudentexpensetrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentexpensetrackerApplication.class, args);
	}

}
