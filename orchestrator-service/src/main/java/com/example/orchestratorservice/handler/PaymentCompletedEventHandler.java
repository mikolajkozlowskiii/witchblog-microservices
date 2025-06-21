package com.example.orchestratorservice.handler;

import com.example.orchestratorservice.component.CreateChatGPTRequestPayloadAutoTask;
import com.example.orchestratorservice.model.DivinationProcess;
import org.common.eventing.process.event.ProcessEndedEvent;
import org.common.model.DivinationProcessStatus;
import com.example.orchestratorservice.repository.DivinationProcessRepository;
import lombok.AllArgsConstructor;
import org.common.eventing.core.handler.EventHandler;
import org.common.eventing.core.model.Event;
import org.common.eventing.payment.event.PaymentCompletedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class PaymentCompletedEventHandler implements EventHandler<PaymentCompletedEvent> {
    private final DivinationProcessRepository divinationProcessRepository;
    private final KafkaTemplate<String, Event> kafkaTemplate;
    private final CreateChatGPTRequestPayloadAutoTask createChatGPTRequestPayloadAutoTask;


    @Override
    public void handle(PaymentCompletedEvent event) {
        switch (event.state()) {
            case PAYMENT_SUCCEEDED -> handleSuccess(event);
            case PAYMENT_FAILED_BUSINESS_ERROR, PAYMENT_FAILED_TECHNICAL_ERROR -> handleError(event);
        }
    }

    private void handleSuccess(PaymentCompletedEvent event) {
        divinationProcessRepository.updateStatusById(UUID.fromString(event.processId()), DivinationProcessStatus.PaymentAccepted);
        createChatGPTRequestPayloadAutoTask.createChatGPTPayloadAndGetDivination(event.userId(), event.processId());
    }

    private void handleError(PaymentCompletedEvent event) {
        DivinationProcess divinationProcess = divinationProcessRepository
                .findById(UUID.fromString(event.processId()))
                .orElseThrow();

        divinationProcess.setStatus(DivinationProcessStatus.FinishedWithWrongPaymentStatus);
        divinationProcess.setStatusComment(event.message());
        divinationProcessRepository.save(divinationProcess);

        kafkaTemplate.send("frontend", new ProcessEndedEvent(divinationProcess.getStatus(), divinationProcess.getStatusComment(), divinationProcess.getUserId().toString(), divinationProcess.getId().toString()));
    }
}
