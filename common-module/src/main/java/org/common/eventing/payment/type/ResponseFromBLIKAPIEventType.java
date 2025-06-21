package org.common.eventing.payment.type;

import org.common.eventing.core.model.EventType;

public class ResponseFromBLIKAPIEventType extends EventType {
    public ResponseFromBLIKAPIEventType() {
        super("payment.blik.api.response");
    }
}
