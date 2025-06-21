package org.common.eventing.process.event;

import org.common.eventing.core.model.Event;
import org.common.eventing.core.model.EventType;
import org.common.eventing.process.type.ProcessStartedEventType;

public record ProcessStartedEvent(String processId) implements Event {
    public static final EventType TYPE = new ProcessStartedEventType();
    @Override
    public EventType getType() {
        return TYPE;
    }
}
