package org.common.eventing.payment.type;

import org.common.eventing.core.model.EventType;

public class IncorrectBLIKCodeEventType extends EventType {
    public IncorrectBLIKCodeEventType() {
        super("payment.blik.incorrect");
    }
}
