package com.example.paymentservice.handler;

import com.example.paymentservice.model.PaymentProcess;
import com.example.paymentservice.repository.PaymentProcessRepository;
import lombok.AllArgsConstructor;
import org.common.eventing.core.handler.EventHandler;
import org.common.eventing.payment.event.RequestPaymentInfoEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class PaymentRequestEventHandler implements EventHandler<RequestPaymentInfoEvent> {
    private final PaymentProcessRepository paymentProcessRepository;

    @Override
    public void handle(RequestPaymentInfoEvent event) {
        if(paymentProcessRepository.findByDivinationIdAndUserId(UUID.fromString(event.processId()), UUID.fromString(event.userId())).isEmpty()) {
            PaymentProcess paymentProcess = new PaymentProcess();
            paymentProcess.setDivinationId(UUID.fromString(event.processId()));
            paymentProcess.setUserId(UUID.fromString(event.userId()));
            paymentProcessRepository.save(paymentProcess);
        }
    }
}