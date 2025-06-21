package com.example.paymentservice.handler;

import com.example.paymentservice.model.PaymentProcess;
import com.example.paymentservice.repository.PaymentProcessRepository;
import lombok.AllArgsConstructor;
import org.common.eventing.core.handler.EventHandler;
import org.common.eventing.core.model.Event;
import org.common.eventing.payment.event.PaymentCompletedEvent;
import org.common.eventing.payment.event.ResponseFromBLIKAPIEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class ResponseFromBLIKEventAPIEventHandler implements EventHandler<ResponseFromBLIKAPIEvent> {
    private final PaymentProcessRepository paymentProcessRepository;
    private final KafkaTemplate<String, Event> kafkaTemplate;

    @Override
    public void handle(ResponseFromBLIKAPIEvent event) {
        PaymentProcess paymentProcess = paymentProcessRepository
                .findByDivinationIdAndUserId(UUID.fromString(event.processId()), UUID.fromString(event.userId()))
                .orElseThrow(() -> new RuntimeException("Could not find payment process"));
        paymentProcess.setPaymentState(event.state());
        paymentProcess.setStatus(event.message());
        paymentProcessRepository.save(paymentProcess);

        PaymentCompletedEvent completedEvent = new PaymentCompletedEvent(event.processId(), event.userId(), event.state(), event.message());

        kafkaTemplate.send("frontend", completedEvent);
        kafkaTemplate.send("orchestrator", completedEvent);
    }
}
