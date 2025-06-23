package com.example.orchestratorservice.component;

import com.example.orchestratorservice.model.DivinationProcess;
import com.example.orchestratorservice.repository.DivinationProcessRepository;
import org.common.eventing.gpt.tarot.TarotCard;
import lombok.RequiredArgsConstructor;
import org.common.eventing.core.model.Event;
import org.common.eventing.gpt.event.DivinationRequestedEvent;
import org.common.model.UserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateChatGPTRequestPayloadAutoTask {

    @Value("${tarot-card.number:3}")
    private Integer numberOfCards;
    private final TarotCardGenerator tarotCardGenerator;
    private final KafkaTemplate<String, Event> kafkaTemplate;
    private final DivinationProcessRepository processRepository;

    public void createChatGPTPayloadAndGetDivination(String userId, String processId, UserInfo userInfo) {
        List<TarotCard> cards = tarotCardGenerator.generateNRandomTarotCards(numberOfCards);

        DivinationProcess process = processRepository
                .findById(UUID.fromString(processId))
                .orElseThrow(() -> new RuntimeException("Process not found"));

        process.setTarotCards(cards);
        processRepository.save(process);

        Event event = new DivinationRequestedEvent(cards, userId, processId, userInfo);
        kafkaTemplate.send("divination", event);
        kafkaTemplate.send("frontend", event);
    }
}
