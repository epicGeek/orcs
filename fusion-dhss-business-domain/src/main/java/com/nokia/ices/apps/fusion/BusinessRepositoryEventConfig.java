package com.nokia.ices.apps.fusion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nokia.ices.apps.fusion.command.event.CommandCheckItemEvent;
import com.nokia.ices.apps.fusion.equipment.event.EquipmentNeEvent;
import com.nokia.ices.apps.fusion.equipment.event.EquipmentNumberSectionEvent;
import com.nokia.ices.apps.fusion.system.event.SystemMenuEvent;

@Configuration
public class BusinessRepositoryEventConfig {

    @Bean
    CommandCheckItemEvent commandCheckItemEventHandler() {
        return new CommandCheckItemEvent();
    }
    
//    @Bean
//    CommandGroupEvent commandGroupEventHandler() {
//        return new CommandGroupEvent();
//    }
    
    @Bean
    EquipmentNeEvent equipmentNeEventHandler() {
        return new EquipmentNeEvent();
    }
    
    @Bean
    EquipmentNumberSectionEvent equipmentNumberSectionEventHandler() {
        return new EquipmentNumberSectionEvent();
    }
    
    @Bean
    SystemMenuEvent SystemMenuEventHandler() {
        return new SystemMenuEvent();
    }

}
