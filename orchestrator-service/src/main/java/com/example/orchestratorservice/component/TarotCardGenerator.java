package com.example.orchestratorservice.component;

import org.common.eventing.gpt.tarot.TarotCard;
import org.common.eventing.gpt.tarot.TarotCardsLookupTable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Component
public class TarotCardGenerator {

    @Value("${tarot-card.reverse-probability:0.3}")
    private double reverseProbability;

    public List<TarotCard> generateNRandomTarotCards(int n) {
        if(n < 0 || n > TarotCardsLookupTable.tarotCards.size() - 1){
            throw new IllegalArgumentException("n must be between 0 and " + TarotCardsLookupTable.tarotCards.size());
        }

        Random random = new Random();

        List<TarotCard> shuffled = new ArrayList<>(TarotCardsLookupTable.tarotCards);
        Collections.shuffle(shuffled);

        List<TarotCard> drawnCards = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            TarotCard tarotCard = shuffled.get(i);
            boolean isReversed = random.nextDouble() < reverseProbability;
            TarotCard finalCard = new TarotCard(tarotCard.cardName(), tarotCard.description(), isReversed);
            drawnCards.add(finalCard);
        }
        return drawnCards;
    }
}

