package org.common.eventing.gpt.event;

import org.common.eventing.core.model.Event;
import org.common.eventing.core.model.EventType;
import org.common.eventing.gpt.type.SendTokenConsumptionInfoEventType;

public record SendTokenConsumptionInfoEvent(int inputTokenConsumption, int outputTokenConsumption) implements Event {
    public static final EventType TYPE = new SendTokenConsumptionInfoEventType();
    @Override
    public EventType getType() {
        return TYPE;
    }
}
