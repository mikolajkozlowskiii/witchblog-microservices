package org.common.eventing.gpt.tarot;

import java.util.List;

public class TarotCardsLookupTable {
    public static final List<TarotCard> tarotCards = List.of(
            new TarotCard("death", "Transformation, endings, rebirth."),
            new TarotCard("devil", "Dependency, materialism, temptation."),
            new TarotCard("justice", "Balance, truth, fairness."),
            new TarotCard("lovers", "Relationships, choices, harmony."),
            new TarotCard("moon", "Illusion, fear, subconscious."),
            new TarotCard("star", "Hope, inspiration, healing."),
            new TarotCard("sun", "Joy, success, optimism."),
            new TarotCard("tower", "Sudden change, upheaval, awakening."),
            new TarotCard("wheel", "Fate, change, cycles.")
    );
}