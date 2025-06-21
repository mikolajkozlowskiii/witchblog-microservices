package com.example.orchestratorservice.controller;

import org.common.model.DivinationProcessStatus;
import com.example.orchestratorservice.repository.DivinationProcessRepository;
import lombok.AllArgsConstructor;
import org.common.eventing.core.model.Event;
import org.common.eventing.payment.event.RequestPaymentInfoEvent;
import org.common.eventing.process.event.ProcessStartedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final KafkaTemplate<String, Event> kafkaTemplate;
    private final DivinationProcessRepository divinationProcessRepository;

    @MessageMapping("/register")
    public void register(Principal principal) throws InterruptedException {
        String sessionId = principal.getName();
        String userId = sessionId.split(":")[0];
        String processId = sessionId.split(":")[1];
        divinationProcessRepository.updateStatusById(UUID.fromString(processId), DivinationProcessStatus.Pending);
        Thread.sleep(1000); // brzydkie, ale działa :D
        messagingTemplate.convertAndSendToUser(sessionId, "/topic/messages", new ProcessStartedEvent(sessionId));
        kafkaTemplate.send("payment", new RequestPaymentInfoEvent(userId, processId));
    }
}
