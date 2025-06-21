package org.common.eventing.core.registry;

import jakarta.annotation.PostConstruct;
import org.common.eventing.core.model.Event;
import org.common.eventing.core.model.EventType;
import org.common.eventing.core.handler.EventHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EventHandlerRegistry {
    private final Map<EventType, EventHandler<? extends Event>> registry = new HashMap<>();
    private final ApplicationContext applicationContext;

    public EventHandlerRegistry(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        Map<String, EventHandler> handlers = applicationContext.getBeansOfType(EventHandler.class);
        for (EventHandler handler : handlers.values()) {
            Class<?> eventClass = GenericTypeResolver.resolveTypeArgument(handler.getClass(), EventHandler.class);
            if (eventClass == null || !Event.class.isAssignableFrom(eventClass)) {
                throw new IllegalArgumentException("Invalid handler: " + handler.getClass());
            }
            try {
                EventType eventType = (EventType) eventClass.getField("TYPE").get(null);
                registry.put(eventType, handler);
            } catch (Exception e) {
                throw new RuntimeException("Failed to register handler: " + handler.getClass(), e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> EventHandler<T> getHandler(EventType eventType) {
        return (EventHandler<T>) registry.get(eventType);
    }

    public boolean hasHandler(EventType eventType) {
        return registry.containsKey(eventType);
    }
}