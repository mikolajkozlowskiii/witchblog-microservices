package com.example.orchestratorservice.websocket;

import jakarta.ws.rs.BadRequestException;
import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.net.URI;
import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final RegistrableProcessHandshakeInterceptor registrableProcessHandshakeInterceptor;
    private final static String USER_ID_PARAMETER = "userId=";
    private final static String PROCESS_ID_PARAMETER = "processId=";

    public WebSocketConfig(RegistrableProcessHandshakeInterceptor registrableProcessHandshakeInterceptor) {
        this.registrableProcessHandshakeInterceptor = registrableProcessHandshakeInterceptor;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                .addInterceptors(registrableProcessHandshakeInterceptor)
                .setHandshakeHandler(new DefaultHandshakeHandler() {
                    @Override
                    protected Principal determineUser(@NonNull ServerHttpRequest request, @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) {
                        return () -> getParameterFromRequest(request, USER_ID_PARAMETER) + ":" + getParameterFromRequest(request, PROCESS_ID_PARAMETER);
                    }
                })
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    private String getParameterFromRequest(ServerHttpRequest request, String parameter) {
        URI uri = request.getURI();
        String query = uri.getQuery();
        if (query != null) {
            for (String param : query.split("&")) {
                if (param.startsWith(parameter)) {
                    return param.substring(parameter.length());
                }
            }
        }
        throw new BadRequestException("Missing parameter");

    }
}
