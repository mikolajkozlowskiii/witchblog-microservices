package org.common.eventing.payment.event;

import org.common.eventing.core.model.Event;
import org.common.eventing.core.model.EventType;
import org.common.eventing.payment.type.IncorrectBLIKCodeEventType;

public record IncorrectBLIKCodeEvent(String message, String processId, String userId) implements Event {
    public static final EventType TYPE = new IncorrectBLIKCodeEventType();

    @Override
    public EventType getType() {
        return TYPE;
    }
}
