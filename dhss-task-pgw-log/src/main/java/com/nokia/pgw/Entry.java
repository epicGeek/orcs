package com.nokia.pgw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Entry {
	private static final Logger LOGGER = LoggerFactory.getLogger(Entry.class);
	public static void main(String[] args) {
		SpringApplication.run(Entry.class);
	}
}
