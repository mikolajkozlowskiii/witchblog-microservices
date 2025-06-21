package org.common.eventing.payment.event;

import org.common.eventing.core.model.Event;
import org.common.eventing.core.model.EventType;
import org.common.eventing.payment.type.ResponseFromBLIKAPIEventType;
import org.common.model.PaymentState;

public record ResponseFromBLIKAPIEvent(String userId, String processId, PaymentState state, String message) implements Event {
    public static final EventType TYPE = new ResponseFromBLIKAPIEventType();
    @Override
    public EventType getType() {
        return TYPE;
    }
}
