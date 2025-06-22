package com.example.divinationservice.dto;

import org.common.eventing.gpt.tarot.TarotCard;

import java.time.LocalDateTime;
import java.util.List;


public record DivinationResponseDTO(String content, List<TarotCard> cards, String userId, String processId, LocalDateTime localDateTime) {
}
