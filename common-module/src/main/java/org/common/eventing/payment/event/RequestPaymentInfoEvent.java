package org.common.eventing.payment.event;

import org.common.eventing.core.model.Event;
import org.common.eventing.core.model.EventType;
import org.common.eventing.payment.type.RequestPaymentInfoEventType;

public record RequestPaymentInfoEvent(String userId, String processId) implements Event {

    public static final EventType TYPE = new RequestPaymentInfoEventType();

    @Override
    public EventType getType() {
        return TYPE;
    }
}
