package com.nokia.ices.apps.fusion.system;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nokia.ices.apps.fusion.system.event.SystemAreaEvent;
import com.nokia.ices.apps.fusion.system.event.SystemRoleEventHandler;
import com.nokia.ices.apps.fusion.system.event.SystemUserEventHandler;

@Configuration
public class SystemRepositoryEventConfig {

    @Bean
    SystemUserEventHandler systemUserEventHandler() {
        return new SystemUserEventHandler();
    }

    @Bean
    SystemRoleEventHandler systemRoleEventHandler() {
        return new SystemRoleEventHandler();
    }
    
    @Bean
    SystemAreaEvent SystemAreaEventHandler() {
        return new SystemAreaEvent();
    }
}
