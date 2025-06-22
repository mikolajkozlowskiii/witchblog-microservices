package com.example.divinationservice.dto;

import org.common.eventing.gpt.tarot.TarotCard;
import org.common.model.UserInfo;

import java.time.LocalDateTime;
import java.util.List;

public record DivinationRequestDTO(List<TarotCard> cards, String userId, String processId, UserInfo userInfo, LocalDateTime localDateTime) {
}
