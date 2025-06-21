package org.common.eventing.process.type;

import org.common.eventing.core.model.EventType;

public class ProcessStartedEventType extends EventType {

    public ProcessStartedEventType() {
        super("process.started");
    }
}
