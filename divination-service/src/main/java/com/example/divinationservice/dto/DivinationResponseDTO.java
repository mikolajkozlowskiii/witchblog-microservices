package com.example.divinationservice.dto;

import org.common.eventing.gpt.tarot.TarotCard;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public record DivinationResponseDTO(String content, List<TarotCard> cards, String userId, String processId, LocalDateTime localDateTime) {
}
