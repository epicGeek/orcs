package com.dhss.app.boss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class BossCounterApp {
	private static final Logger logger = LoggerFactory.getLogger(BossCounterApp.class);
	public static void main(String[] args) {
		logger.info("BOSS counter starts.");
		SpringApplication.run(BossCounterApp.class);
	}
}