package com.nokia.ices.apps.notifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.nokia.ices.apps.notifier.service.impl.NotificationSMSServiceImpl;

@SpringBootApplication
public class IcesNotifierSMSApplication {
	private static final Logger logger = LoggerFactory.getLogger(NotificationSMSServiceImpl.class);

	public static void main(String[] args) {
		logger.info("hello ,sending message!");
		SpringApplication.run(IcesNotifierSMSApplication.class);
	}
}
