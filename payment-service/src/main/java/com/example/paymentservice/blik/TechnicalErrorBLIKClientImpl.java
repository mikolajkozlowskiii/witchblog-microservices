package com.example.paymentservice.blik;

import com.example.paymentservice.repository.PaymentProcessRepository;
import org.common.eventing.core.model.Event;
import org.common.eventing.payment.event.ResponseFromBLIKAPIEvent;
import org.common.model.PaymentState;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name="blik-service", havingValue="technical-error")
public class TechnicalErrorBLIKClientImpl extends BLIKClient {
    public TechnicalErrorBLIKClientImpl(KafkaTemplate<String, Event> kafkaTemplate, PaymentProcessRepository paymentProcessRepository) {
        super(kafkaTemplate, paymentProcessRepository);
    }

    @Override
    public void payWithBLIK(String code, String userId, String processId) {
        ResponseFromBLIKAPIEvent event = new ResponseFromBLIKAPIEvent(userId, processId, PaymentState.PAYMENT_FAILED_TECHNICAL_ERROR, "Integration with BLIK failed. Service temporary unavailable.");
        kafkaTemplate.send("payment", event);
    }
}
