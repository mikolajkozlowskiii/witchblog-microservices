package com.example.divinationservice.service;

import com.example.divinationservice.component.PromptBuilder;
import com.example.divinationservice.dto.DivinationGenerationResult;
import com.example.divinationservice.dto.DivinationRequestDTO;
import com.example.divinationservice.dto.DivinationResponseDTO;
import com.example.divinationservice.model.DivinationProcess;
import com.example.divinationservice.repository.DivinationProcessRepository;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.eventing.core.model.Event;
import org.common.eventing.gpt.event.DivinationGenerationEvent;
import org.common.model.DivinationGenerationStatus;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DivinationServiceImpl implements DivinationService{
    private final ChatModel chatModel;
    private final PromptBuilder promptBuilder;
    private final KafkaTemplate<String, Event> kafkaTemplate;
    private final DivinationProcessRepository repository;

    @Override
    public DivinationGenerationResult generateDivination(DivinationRequestDTO divinationRequestDTO) {
        String prompt = promptBuilder.buildPrompt(divinationRequestDTO);
        log.debug("Prompt for userId={}, processId={}: \n{}", divinationRequestDTO.userId(), divinationRequestDTO.processId(), prompt);

        String llmAnswer = chatModel.chat(prompt);
        log.info("LLM responded for processId={} (userId={})", divinationRequestDTO.processId(), divinationRequestDTO.userId());

        log.info("Divination: {}", llmAnswer);

        DivinationResponseDTO dto = new DivinationResponseDTO(llmAnswer, divinationRequestDTO.cards(), divinationRequestDTO.userId(), divinationRequestDTO.processId(), LocalDateTime.now());

        String modelName = chatModel instanceof OpenAiChatModel oai
                ? oai.defaultRequestParameters().modelName()
                : chatModel.getClass().getSimpleName();

        return new DivinationGenerationResult(dto, prompt, modelName);
    }

    @Override
    @Transactional
    public Optional<String> retryIntegration(UUID processId) {
        log.info("Retry divination process for process id: {}", processId);
        try {
            DivinationProcess process = repository
                    .getDivinationProcessByProcessId(processId)
                    .orElseThrow(() -> new NoSuchElementException("Not found divination with process id: " + processId.toString()));

            if (!process.getStatus().equals(DivinationGenerationStatus.FAILURE.name())){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Divination is in wrong state");
            }
            log.info("Found Divination Process for id {}", processId);

            final String llmAnswer = chatModel.chat(process.getPrompt());

            log.info("LLM responded for processId={} (userId={})", process.getProcessId(), process.getUserId());

            log.info("Divination: {}", llmAnswer);

            process.setResponse(llmAnswer);
            process.setStatus(DivinationGenerationStatus.SUCCESS.name());
            repository.save(process);
            kafkaTemplate.send("orchestrator", new DivinationGenerationEvent(llmAnswer, DivinationGenerationStatus.SUCCESS, processId.toString(), process.getUserId().toString()));
            return Optional.of(llmAnswer);
        } catch (Exception exception) {
            return Optional.empty();
        }

    }
}
