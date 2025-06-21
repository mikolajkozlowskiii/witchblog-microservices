package com.example.orchestratorservice.websocket;

import lombok.AllArgsConstructor;
import org.common.eventing.core.model.Event;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
@AllArgsConstructor
public class KafkaWebSocketBridge {
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "frontend", groupId = "orchestrator-group")
    public void passToFrontend(Event event) throws IllegalAccessException, NoSuchFieldException {
        Class<?> clazz = event.getClass();

        Field userIdField = clazz.getDeclaredField("userId");
        Field processIdField = clazz.getDeclaredField("processId");

        userIdField.setAccessible(true);
        processIdField.setAccessible(true);

        String userId = (String) userIdField.get(event);
        String processId = (String) processIdField.get(event);
        messagingTemplate.convertAndSendToUser(userId + ":" + processId, "/topic/messages", event);
    }
}
