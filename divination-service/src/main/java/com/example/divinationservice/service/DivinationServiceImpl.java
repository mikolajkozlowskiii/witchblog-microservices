package com.example.divinationservice.service;

import com.example.divinationservice.component.PromptBuilder;
import com.example.divinationservice.dto.DivinationGenerationResult;
import com.example.divinationservice.dto.DivinationRequestDTO;
import com.example.divinationservice.dto.DivinationResponseDTO;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DivinationServiceImpl implements DivinationService{
    private final ChatModel chatModel;
    private final PromptBuilder promptBuilder;
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
}
