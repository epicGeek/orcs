package com.nokia.ices.apps.fusion.patrol;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nokia.ices.apps.fusion.patrol.event.SmartCheckJobEventHandler;

@Configuration
public class PatrolRepositoryEventConfig {

    @Bean
    SmartCheckJobEventHandler smartCheckJobEventHandler() {
        return new SmartCheckJobEventHandler();
    }
}