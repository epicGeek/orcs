package com.nokia.ices.apps.fusion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
	private static final Logger log = LoggerFactory.getLogger(App.class);
	public static void main(String[] args) {
		log.info("This is a report generator,ready to start!!!!!!!");
		SpringApplication.run(App.class);
	}
}
