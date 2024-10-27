package com.example.quanlisanbay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.example.quanlisanbay.model")
@EnableJpaRepositories("com.example.quanlisanbay.repository")
public class QuanlisanbayApplication {
	public static void main(String[] args) {
		SpringApplication.run(QuanlisanbayApplication.class, args);
	}
}