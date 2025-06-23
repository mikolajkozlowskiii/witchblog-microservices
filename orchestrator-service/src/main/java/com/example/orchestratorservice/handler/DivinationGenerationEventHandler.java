package com.example.orchestratorservice.handler;

import org.common.component.RandomIntegrationFailMessageGenerator;
import com.example.orchestratorservice.model.DivinationProcess;
import com.example.orchestratorservice.repository.DivinationProcessRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.eventing.core.handler.EventHandler;
import org.common.eventing.core.model.Event;
import org.common.eventing.gpt.event.DivinationGenerationEvent;
import org.common.eventing.process.event.ProcessEndedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.common.model.DivinationGenerationStatus.FAILURE;
import static org.common.model.DivinationProcessStatus.FailedIntegrationWithChatGPT;
import static org.common.model.DivinationProcessStatus.Finished;

@Component
@AllArgsConstructor
@Slf4j
public class DivinationGenerationEventHandler implements EventHandler<DivinationGenerationEvent> {

    private final DivinationProcessRepository divinationProcessRepository;
    private final KafkaTemplate<String, Event> kafkaTemplate;

    @Override
    public void handle(DivinationGenerationEvent event) {
        DivinationProcess process = divinationProcessRepository
                .findById(UUID.fromString(event.processId()))
                .orElseThrow(() -> new IllegalArgumentException("Process not found"));

        log.info("Persisting divination for processId={} | status={} | preview=\"{}\"",
                event.processId(), event.status(), event.divination());

        process.setDivination(event.divination());

        String message;
        if(event.status().equals(FAILURE)) {
            process.setStatus(FailedIntegrationWithChatGPT);
            message = RandomIntegrationFailMessageGenerator.getRandomFortuneFail();
        } else{
             process.setStatus(Finished);
             message = "Process finished successfully";
        }


        divinationProcessRepository.save(process);
        kafkaTemplate.send("frontend", new ProcessEndedEvent(process.getStatus(), message, process.getUserId().toString(), process.getId().toString()));
    }
}
