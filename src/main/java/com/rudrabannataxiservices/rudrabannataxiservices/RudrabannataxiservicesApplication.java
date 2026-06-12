package com.rudrabannataxiservices.rudrabannataxiservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class RudrabannataxiservicesApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(RudrabannataxiservicesApplication.class);
	}
	public static void main(String[] args) {
		SpringApplication.run(RudrabannataxiservicesApplication.class, args);
	}

}
