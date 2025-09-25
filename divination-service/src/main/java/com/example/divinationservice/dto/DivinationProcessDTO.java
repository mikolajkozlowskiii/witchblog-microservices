package com.example.divinationservice.dto;

import org.common.eventing.gpt.tarot.TarotCard;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record DivinationProcessDTO(
        Long id,
        UUID divinationId,
        UUID processId,
        List<TarotCard> tarotCards,
        UUID userId,
        String status,
        String prompt,
        String llmModel,
        String response,
        LocalDateTime createdAt
) {}
