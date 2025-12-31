package com.visitly.myproject;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableCaching
@OpenAPIDefinition(
		info = @Info(
				title = "User Management System with RBAC",
				version = "1.0.0",
				description = "Production-ready User Management System with Role-Based Access Control"
		)
)
public class MyprojectApplication {
	public static void main(String[] args) {
		SpringApplication.run(MyprojectApplication.class, args);
	}
}