package com.example.paymentservice.component;

import lombok.RequiredArgsConstructor;
import org.common.event.PaymentCompletedEvent;
import org.common.event.PaymentRequestEvent;
import org.common.event.PaymentSessionCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentRequestListener {
    private final KafkaTemplate<String, PaymentSessionCreatedEvent> kafkaTemplate;

    @KafkaListener(topics = "PaymentRequestTopic", groupId = "payment-group")
    public void handle(PaymentRequestEvent event) {
        String paymentSessionUrl = "http://fake-payment.com/session/" + UUID.randomUUID();
        PaymentSessionCreatedEvent sessionEvent = new PaymentSessionCreatedEvent(event.getJobId(), paymentSessionUrl);
        kafkaTemplate.send("PaymentSessionCreatedTopic", sessionEvent);
    }

}
