package org.common.eventing.payment.event;

import org.common.eventing.core.model.Event;
import org.common.eventing.core.model.EventType;
import org.common.eventing.payment.type.PaymentCompletedEventType;
import org.common.model.PaymentState;

public record PaymentCompletedEvent(String processId, String userId, PaymentState state, String message) implements Event {

    public static final EventType TYPE = new PaymentCompletedEventType();
    @Override
    public EventType getType() {
        return TYPE;
    }
}
