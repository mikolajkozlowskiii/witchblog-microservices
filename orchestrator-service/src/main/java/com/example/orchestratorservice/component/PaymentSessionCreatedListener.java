package com.example.orchestratorservice.component;

import lombok.RequiredArgsConstructor;
import org.common.event.PaymentSessionCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class PaymentSessionCreatedListener {
    private final PaymentSessionWaiter waiter;

    @KafkaListener(topics = "PaymentSessionCreatedTopic", groupId = "orchestrator-group")
    public void listen(PaymentSessionCreatedEvent event) {
        System.out.println("Received PaymentSessionCreatedEvent: " + event);
        waiter.complete(event.getJobId(), event);
    }
}