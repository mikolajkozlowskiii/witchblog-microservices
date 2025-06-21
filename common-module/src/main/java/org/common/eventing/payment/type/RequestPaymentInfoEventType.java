package org.common.eventing.payment.type;

import org.common.eventing.core.model.EventType;

public class RequestPaymentInfoEventType extends EventType {
    public RequestPaymentInfoEventType() {
        super("payment.request");
    }
}
