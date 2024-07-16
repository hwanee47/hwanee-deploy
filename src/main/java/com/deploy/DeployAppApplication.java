package com.deploy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class DeployAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeployAppApplication.class, args);
	}

}
