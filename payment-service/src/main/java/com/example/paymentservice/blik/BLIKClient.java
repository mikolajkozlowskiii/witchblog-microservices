package com.example.paymentservice.blik;

import com.example.paymentservice.repository.PaymentProcessRepository;
import org.common.eventing.core.model.Event;
import org.common.model.PaymentState;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

public abstract class BLIKClient {
    protected final KafkaTemplate<String, Event> kafkaTemplate;
    protected final PaymentProcessRepository paymentProcessRepository;

    protected BLIKClient(KafkaTemplate<String, Event> kafkaTemplate, PaymentProcessRepository paymentProcessRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.paymentProcessRepository = paymentProcessRepository;
    }

    abstract public void payWithBLIK(String code, String userId, String processId);

    public void updateProcess(String processId, PaymentState paymentState) {
        var process = paymentProcessRepository
                .findById(UUID.fromString(processId))
                .orElseThrow(() -> new RuntimeException("Process not found"));
        process.setPaymentState(paymentState);
        paymentProcessRepository.save(process);
    }
}
