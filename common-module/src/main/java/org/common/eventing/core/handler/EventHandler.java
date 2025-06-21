package org.common.eventing.core.handler;

import org.common.eventing.core.model.Event;

public interface EventHandler<T extends Event> {
    void handle(T event);
}