package com.example.managementservice.handler;

import com.example.managementservice.model.Payment;
import com.example.managementservice.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import org.common.eventing.core.handler.EventHandler;
import org.common.eventing.payment.event.ResponseFromBLIKAPIEvent;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ResponseFromBLIKAPIEventHandler implements EventHandler<ResponseFromBLIKAPIEvent> {

    private final PaymentRepository paymentRepository;

    @Override
    public void handle(ResponseFromBLIKAPIEvent event) {
        paymentRepository.save(new Payment());
    }
}
