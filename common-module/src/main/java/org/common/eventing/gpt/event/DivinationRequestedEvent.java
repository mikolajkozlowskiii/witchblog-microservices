package org.common.eventing.gpt.event;

import org.common.eventing.core.model.Event;
import org.common.eventing.core.model.EventType;
import org.common.eventing.gpt.tarot.TarotCard;
import org.common.eventing.gpt.type.DivinationRequestedEventType;

import java.util.List;

public record DivinationRequestedEvent(List<TarotCard> cards, String userId, String processId) implements Event {

    public static final EventType TYPE = new DivinationRequestedEventType();
    @Override
    public EventType getType() {
        return TYPE;
    }
}
