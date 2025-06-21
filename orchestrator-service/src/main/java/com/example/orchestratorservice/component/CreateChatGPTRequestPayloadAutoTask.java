package com.example.orchestratorservice.component;

import org.common.eventing.gpt.tarot.TarotCard;
import lombok.RequiredArgsConstructor;
import org.common.eventing.core.model.Event;
import org.common.eventing.gpt.event.DivinationRequestedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateChatGPTRequestPayloadAutoTask {

    @Value("${tarot-card.number:3}")
    private Integer numberOfCards;
    private final TarotCardGenerator tarotCardGenerator;
    private final KafkaTemplate<String, Event> kafkaTemplate;

    public void createChatGPTPayloadAndGetDivination(String userId, String processId) {
        List<TarotCard> cards = tarotCardGenerator.generateNRandomTarotCards(numberOfCards);
        Event event = new DivinationRequestedEvent(cards, userId, processId);
        kafkaTemplate.send("orchestrator", event);
        kafkaTemplate.send("frontend", event);
    }
}
