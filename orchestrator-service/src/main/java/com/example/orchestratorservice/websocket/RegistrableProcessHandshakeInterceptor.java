package com.example.orchestratorservice.websocket;

import org.common.model.DivinationProcessStatus;
import com.example.orchestratorservice.repository.DivinationProcessRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@Component
@AllArgsConstructor
public class RegistrableProcessHandshakeInterceptor implements HandshakeInterceptor {

    private final DivinationProcessRepository divinationProcessRepository;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {

        URI uri = request.getURI();
        String query = uri.getQuery();
        String userId = null;
        String processId = null;

        if (query != null) {
            for (String param : query.split("&")) {
                if (param.startsWith("userId=")) userId = param.substring("userId=".length());
                if (param.startsWith("processId=")) processId = param.substring("processId=".length());
            }
        }

        return userId != null && processId != null && isProcessInStartedState(processId, userId);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }

    private boolean isProcessInStartedState(String processId, String userId) {
        return divinationProcessRepository
                .findById(UUID.fromString(processId))
                .filter(divinationProcess -> divinationProcess.getStatus() == DivinationProcessStatus.Started)
                .filter(divinationProcess -> divinationProcess.getUserId().toString().equals(userId))
                .isPresent();
    }
}
