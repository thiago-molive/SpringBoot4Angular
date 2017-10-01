package com.springboot4rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.springboot4rest.property.PropertiesConfig;

@SpringBootApplication
@EnableConfigurationProperties(PropertiesConfig.class)
public class Spring4RestApplication {

	public static void main(String[] args) {
		SpringApplication.run(Spring4RestApplication.class, args);
	}
}
