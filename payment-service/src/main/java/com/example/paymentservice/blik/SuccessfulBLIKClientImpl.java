package com.example.paymentservice.blik;

import com.example.paymentservice.repository.PaymentProcessRepository;
import org.common.eventing.core.model.Event;
import org.common.eventing.payment.event.ResponseFromBLIKAPIEvent;
import org.common.model.PaymentState;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name="blik-service", havingValue="success", matchIfMissing=true)
public class SuccessfulBLIKClientImpl extends BLIKClient {

    public SuccessfulBLIKClientImpl(KafkaTemplate<String, Event> kafkaTemplate, PaymentProcessRepository paymentProcessRepository) {
        super(kafkaTemplate, paymentProcessRepository);
    }

    @Override
    public void payWithBLIK(String code, String userId, String processId) {
        ResponseFromBLIKAPIEvent event = new ResponseFromBLIKAPIEvent(userId, processId, PaymentState.PAYMENT_SUCCEEDED, null);
        kafkaTemplate.send("payment", event);
        kafkaTemplate.send("management", event);
    }
}
