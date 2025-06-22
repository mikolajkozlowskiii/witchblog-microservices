package org.common.eventing.gpt.tarot;

import jakarta.persistence.Embeddable;

@Embeddable
public record TarotCard(String cardName, String description, boolean isReversed) {
    TarotCard(String cardName, String description){
        this(cardName,description,false);
    }

    //JPA requires no args constructor
    public TarotCard() {
        this("","",false);
    }
}
