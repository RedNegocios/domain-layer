package com.api.red.negocios.Configuraciones;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws")
        .setAllowedOrigins("http://localhost:3000") // más seguro y explícito
        .withSockJS();

	}


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // lo que escucha el frontend
        config.setApplicationDestinationPrefixes("/app"); // lo que manda el frontend
    }
}

