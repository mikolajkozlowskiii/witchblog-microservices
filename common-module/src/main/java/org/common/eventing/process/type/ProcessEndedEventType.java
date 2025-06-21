package org.common.eventing.process.type;

import org.common.eventing.core.model.EventType;

public class ProcessEndedEventType extends EventType {
    public ProcessEndedEventType() {
        super("process.ended");
    }
}
