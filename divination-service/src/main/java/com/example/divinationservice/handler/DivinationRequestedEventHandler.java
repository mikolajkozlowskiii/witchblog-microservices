package com.example.divinationservice.handler;

import com.example.divinationservice.component.TokenCounter;
import com.example.divinationservice.dto.DivinationGenerationResult;
import com.example.divinationservice.dto.DivinationRequestDTO;
import com.example.divinationservice.model.DivinationProcess;
import com.example.divinationservice.repository.DivinationProcessRepository;
import com.example.divinationservice.service.DivinationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.component.RandomIntegrationFailMessageGenerator;
import org.common.eventing.core.handler.EventHandler;
import org.common.eventing.core.model.Event;
import org.common.eventing.gpt.event.DivinationGenerationEvent;
import org.common.eventing.gpt.event.DivinationRequestedEvent;
import org.common.eventing.gpt.event.SendTokenConsumptionInfoEvent;
import org.common.model.DivinationGenerationStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DivinationRequestedEventHandler implements EventHandler<DivinationRequestedEvent> {

    private static final String FRONTEND_TOPIC     = "frontend";
    private static final String ORCHESTRATOR_TOPIC = "orchestrator";
    private static final List<String> DESTINATION_TOPICS = List.of(FRONTEND_TOPIC, ORCHESTRATOR_TOPIC);

    private final DivinationService divinationService;
    private final DivinationProcessRepository divinationProcessRepository;
    private final KafkaTemplate<String, Event> kafkaTemplate;

    @Override
    public void handle(DivinationRequestedEvent event) {
        log.info("Handling DivinationRequestedEvent [processId={}, userId={}, cards={}, userInfo={}]",
                event.processId(), event.userId(), event.cards(), event.userInfo());

        DivinationRequestDTO divinationRequest = mapToRequest(event);

        try {
            log.debug("Calling divinationService with prompt from request");
            DivinationGenerationResult generationResult =
                    divinationService.generateDivination(divinationRequest);

            log.info("Divination generated successfully for processId={}", event.processId());

            DivinationProcess divinationProcess =
                    mapToEntity(event, generationResult);
            divinationProcessRepository.save(divinationProcess);

            log.info("DivinationProcess persisted to database [divinationId={}, userId={}]",
                    divinationProcess.getDivinationId(), divinationProcess.getUserId());

            Event successEvent = mapToDivinationGenerationEvent(
                    DivinationGenerationStatus.SUCCESS,
                    generationResult.responseDTO().content(),
                    event
            );

            countAndSendTokenUsageToManagementService(generationResult);
            publishEventToTopics(successEvent);

        } catch (Exception exception) {

            log.error("Divination generation failed for processId={}", event.processId(), exception);

            Event failureEvent = mapToDivinationGenerationEvent(
                    DivinationGenerationStatus.FAILURE,
                    RandomIntegrationFailMessageGenerator.getRandomFortuneFail(),
                    event
            );
            publishEventToTopics(failureEvent);
        }
    }

    private void countAndSendTokenUsageToManagementService(DivinationGenerationResult divinationGenerationResult) {
        int promptTokenUsage = TokenCounter.countTokens(divinationGenerationResult.prompt());
        int resultTokenUsage = TokenCounter.countTokens(divinationGenerationResult.responseDTO().content());

        Event event = new SendTokenConsumptionInfoEvent(promptTokenUsage, resultTokenUsage);

        kafkaTemplate.send("management", event);
    }


    private DivinationRequestDTO mapToRequest(DivinationRequestedEvent event) {
        return new DivinationRequestDTO(
                event.cards(),
                event.userId(),
                event.processId(),
                event.userInfo(),
                LocalDateTime.now()
        );
    }

    private DivinationProcess mapToEntity(DivinationRequestedEvent event,
                                          DivinationGenerationResult result) {

        DivinationProcess process = new DivinationProcess();
        process.setDivinationId(UUID.randomUUID());
        process.setProcessId(UUID.fromString(event.processId()));
        process.setTarotCards(event.cards());
        process.setUserId(UUID.fromString(event.userId()));
        process.setStatus(DivinationGenerationStatus.SUCCESS.name());
        process.setPrompt(result.prompt());
        process.setLlmModel(result.modelName());
        process.setResponse(result.responseDTO().content());
        return process;
    }

    private Event mapToDivinationGenerationEvent(DivinationGenerationStatus status,
                                                 String content,
                                                 DivinationRequestedEvent event) {

        return new DivinationGenerationEvent(
                content,
                status,
                event.processId(),
                event.userId()
        );
    }


    private void publishEventToTopics(Event event) {
        DESTINATION_TOPICS.forEach(topic -> kafkaTemplate.send(topic, event));
    }
}
