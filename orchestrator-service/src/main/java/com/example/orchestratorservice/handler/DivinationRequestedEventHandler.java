package com.example.orchestratorservice.handler;

import com.example.orchestratorservice.model.DivinationProcess;
import com.example.orchestratorservice.repository.DivinationProcessRepository;
import lombok.AllArgsConstructor;
import org.common.eventing.core.handler.EventHandler;
import org.common.eventing.core.model.Event;
import org.common.eventing.gpt.event.DivinationGenerationEvent;
import org.common.eventing.gpt.event.DivinationRequestedEvent;
import org.common.model.DivinationGenerationStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class DivinationRequestedEventHandler implements EventHandler<DivinationRequestedEvent> {

    private final KafkaTemplate<String, Event> kafkaTemplate;
    private final DivinationProcessRepository divinationProcessRepository;

    @Override
    public void handle(DivinationRequestedEvent event) {
        DivinationProcess process = divinationProcessRepository
                .findById(UUID.fromString(event.processId()))
                .orElseThrow(() -> new IllegalArgumentException("Process not found"));

        process.setTarotCards(event.cards());
        divinationProcessRepository.save(process);

        Event generatedDivination = new DivinationGenerationEvent("Potężna wróżba", DivinationGenerationStatus.SUCCESS, event.processId(), event.userId());
        kafkaTemplate.send("frontend", generatedDivination);
        kafkaTemplate.send("orchestrator", generatedDivination);
    }
}
