package com.example.SABS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.SABS.model")
public class SabsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SabsApplication.class, args);
	}

}
