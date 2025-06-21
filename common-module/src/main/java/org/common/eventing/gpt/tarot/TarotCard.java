package org.common.eventing.gpt.tarot;

public record TarotCard(String cardName, String description, boolean isReversed) {
    TarotCard(String cardName, String description){
        this(cardName,description,false);
    }
}
