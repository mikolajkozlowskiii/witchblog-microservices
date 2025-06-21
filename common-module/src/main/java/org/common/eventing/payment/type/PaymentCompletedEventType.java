package org.common.eventing.payment.type;

import org.common.eventing.core.model.EventType;

public class PaymentCompletedEventType extends EventType {
    public PaymentCompletedEventType() {
        super("payment.completed");
    }
}
