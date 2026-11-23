package com.ahmed.Security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.from(SecurityApplication::main).with(TestcontainersConfiguration.class).run(args);
	}
}