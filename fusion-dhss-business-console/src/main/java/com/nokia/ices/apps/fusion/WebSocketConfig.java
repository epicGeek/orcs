package com.nokia.ices.apps.fusion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.nokia.ices.apps.fusion.websocket.SecureShellWS;

@Configuration
public class WebSocketConfig {
    @Bean
    public SecureShellWS secureShellWS() {
        return new SecureShellWS();
    }
//    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
