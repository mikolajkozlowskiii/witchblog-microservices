package org.common.eventing.gpt.event;

import org.common.eventing.core.model.Event;
import org.common.eventing.core.model.EventType;
import org.common.eventing.gpt.type.DivinationGenerationEventType;
import org.common.model.DivinationGenerationStatus;

public record DivinationGenerationEvent(String divination, DivinationGenerationStatus status, String processId, String userId) implements Event {

    public static final EventType TYPE = new DivinationGenerationEventType();
    @Override
    public EventType getType() {
        return TYPE;
    }
}
