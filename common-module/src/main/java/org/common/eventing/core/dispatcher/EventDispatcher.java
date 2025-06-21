package org.common.eventing.core.dispatcher;

import org.common.eventing.core.model.Event;
import org.common.eventing.core.model.EventType;
import org.common.eventing.core.handler.EventHandler;
import org.common.eventing.core.registry.EventHandlerRegistry;
import org.springframework.stereotype.Component;

@Component
public class EventDispatcher {

    private final EventHandlerRegistry registry;

    public EventDispatcher(EventHandlerRegistry registry) {
        this.registry = registry;
    }

    public void dispatch(Event event) {
        EventType type = event.getType();
        if (!registry.hasHandler(type)) {
            throw new IllegalStateException("No handler registered for eventing type: " + type);
        }
        EventHandler<Event> handler = registry.getHandler(type);
        handler.handle(event);
    }
}