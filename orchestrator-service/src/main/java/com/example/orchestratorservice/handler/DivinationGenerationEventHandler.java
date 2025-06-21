package com.example.orchestratorservice.handler;

import com.example.orchestratorservice.model.DivinationProcess;
import com.example.orchestratorservice.repository.DivinationProcessRepository;
import lombok.AllArgsConstructor;
import org.common.eventing.core.handler.EventHandler;
import org.common.eventing.core.model.Event;
import org.common.eventing.gpt.event.DivinationGenerationEvent;
import org.common.eventing.process.event.ProcessEndedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.common.model.DivinationProcessStatus.Finished;

@Component
@AllArgsConstructor
public class DivinationGenerationEventHandler implements EventHandler<DivinationGenerationEvent> {

    private final DivinationProcessRepository divinationProcessRepository;
    private final KafkaTemplate<String, Event> kafkaTemplate;

    @Override
    public void handle(DivinationGenerationEvent event) {
        DivinationProcess process = divinationProcessRepository
                .findById(UUID.fromString(event.processId()))
                .orElseThrow(() -> new IllegalArgumentException("Process not found"));

        process.setDivination(event.divination());
        process.setStatus(Finished);

        divinationProcessRepository.save(process);
        kafkaTemplate.send("frontend", new ProcessEndedEvent(Finished, "Process finished successfully", process.getUserId().toString(), process.getId().toString()));
    }
}
