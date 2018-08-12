package com.qualogy.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

/**
 * Application class for todoapplication
 */
@EnableSpringDataWebSupport
@SpringBootApplication
public class TodoAppApplication {
	public static void main(final String[] args) {
		SpringApplication.run(TodoAppApplication.class, args);
	}
}
