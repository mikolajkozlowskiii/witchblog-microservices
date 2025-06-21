package com.example.orchestratorservice.handler;

import lombok.AllArgsConstructor;
import org.common.eventing.core.handler.EventHandler;
import org.common.eventing.core.model.Event;
import org.common.eventing.gpt.event.DivinationGenerationEvent;
import org.common.eventing.gpt.event.DivinationRequestedEvent;
import org.common.model.DivinationGenerationStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DivinationRequestedEventHandler implements EventHandler<DivinationRequestedEvent> {

    private final KafkaTemplate<String, Event> kafkaTemplate;

    @Override
    public void handle(DivinationRequestedEvent event) {
        Event generatedDivination = new DivinationGenerationEvent("Potężna wróżba", DivinationGenerationStatus.SUCCESS, event.processId(), event.userId());
        kafkaTemplate.send("frontend", generatedDivination);
        kafkaTemplate.send("orchestrator", generatedDivination);
    }
}
