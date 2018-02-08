package com.nokia.boss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Entry {
	private static final Logger logger = LoggerFactory.getLogger(Entry.class);
	public static void main(String[] args) {
		logger.info("This BOSS task will bring revolution...");
		SpringApplication.run(Entry.class);
	}
}
