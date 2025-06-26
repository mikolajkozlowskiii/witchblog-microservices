package com.example.divinationservice.handler;

import com.example.divinationservice.component.PromptBuilder;
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

    private static final String FRONTEND_TOPIC = "frontend";
    private static final String ORCHESTRATOR_TOPIC = "orchestrator";
    private static final String MANAGEMENT_TOPIC = "management";
    private static final List<String> DESTINATION_TOPICS = List.of(FRONTEND_TOPIC, ORCHESTRATOR_TOPIC);

    private final DivinationService divinationService;
    private final DivinationProcessRepository divinationProcessRepository;
    private final KafkaTemplate<String, Event> kafkaTemplate;
    private final PromptBuilder promptBuilder;

    @Override
    public void handle(DivinationRequestedEvent event) {
        log.info("Handling DivinationRequestedEvent [processId={}, userId={}, cards={}, userInfo={}]",
                event.processId(), event.userId(), event.cards(), event.userInfo());

        DivinationRequestDTO request = mapToRequest(event);

        try {
            DivinationGenerationResult result = divinationService.generateDivination(request);

            log.info("Divination generated successfully for processId={}", event.processId());

            DivinationProcess process = buildDivinationProcess(event, result, DivinationGenerationStatus.SUCCESS);
            divinationProcessRepository.save(process);

            publishEventToTopics(buildDivinationGenerationEvent(event, DivinationGenerationStatus.SUCCESS, result.responseDTO().content()));
            sendTokenUsageToManagement(result);

        } catch (Exception ex) {
            log.error("Divination generation failed for processId={}", event.processId(), ex);

            DivinationProcess failedProcess = buildDivinationProcess(event, null, DivinationGenerationStatus.FAILURE);
            failedProcess.setPrompt(promptBuilder.buildPrompt(request));
            divinationProcessRepository.save(failedProcess);

            publishEventToTopics(buildDivinationGenerationEvent(event, DivinationGenerationStatus.FAILURE, RandomIntegrationFailMessageGenerator.getRandomFortuneFail()));
        }
    }

    private void sendTokenUsageToManagement(DivinationGenerationResult result) {
        int promptTokens = TokenCounter.countTokens(result.prompt());
        int responseTokens = TokenCounter.countTokens(result.responseDTO().content());

        kafkaTemplate.send(MANAGEMENT_TOPIC, new SendTokenConsumptionInfoEvent(promptTokens, responseTokens));
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

    private DivinationProcess buildDivinationProcess(DivinationRequestedEvent event, DivinationGenerationResult result, DivinationGenerationStatus status) {
        DivinationProcess process = new DivinationProcess();
        process.setDivinationId(UUID.randomUUID());
        process.setProcessId(UUID.fromString(event.processId()));
        process.setTarotCards(event.cards());
        process.setUserId(UUID.fromString(event.userId()));
        process.setStatus(status.name());


        if (status == DivinationGenerationStatus.SUCCESS && result != null) {
            process.setPrompt(result.prompt());
            process.setLlmModel(result.modelName());
            process.setResponse(result.responseDTO().content());
        }

        return process;
    }

    private Event buildDivinationGenerationEvent(DivinationRequestedEvent event, DivinationGenerationStatus status, String content) {
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

