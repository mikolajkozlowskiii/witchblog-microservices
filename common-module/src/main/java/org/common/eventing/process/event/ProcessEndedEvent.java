package org.common.eventing.process.event;

import org.common.eventing.core.model.Event;
import org.common.eventing.core.model.EventType;
import org.common.eventing.process.type.ProcessEndedEventType;
import org.common.model.DivinationProcessStatus;

public record ProcessEndedEvent(DivinationProcessStatus status, String message, String userId, String processId) implements Event {

    public static final EventType TYPE = new ProcessEndedEventType();
    @Override
    public EventType getType() {
        return TYPE;
    }
}
