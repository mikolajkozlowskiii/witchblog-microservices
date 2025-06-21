package com.example.paymentservice.blik;

import com.example.paymentservice.repository.PaymentProcessRepository;
import org.common.eventing.core.model.Event;
import org.springframework.kafka.core.KafkaTemplate;

public abstract class BLIKClient {
    protected final KafkaTemplate<String, Event> kafkaTemplate;
    protected final PaymentProcessRepository paymentProcessRepository;

    protected BLIKClient(KafkaTemplate<String, Event> kafkaTemplate, PaymentProcessRepository paymentProcessRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.paymentProcessRepository = paymentProcessRepository;
    }

    abstract public void payWithBLIK(String code, String userId, String processId);
}
