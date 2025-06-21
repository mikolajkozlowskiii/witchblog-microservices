package org.common.eventing.core.registry;

import jakarta.annotation.PostConstruct;
import org.common.eventing.core.model.Event;
import org.common.eventing.core.model.EventType;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EventClassRegistry {

    private final Map<EventType, Class<? extends Event>> eventTypeToClass = new HashMap<>();

    @PostConstruct
    public void init() {
        Reflections reflections = new Reflections("org.common.eventing");
        Set<Class<? extends Event>> eventClasses = reflections.getSubTypesOf(Event.class);
        for (Class<? extends Event> clazz : eventClasses) {
            try {
                EventType eventType = (EventType) clazz.getField("TYPE").get(null);
                eventTypeToClass.put(eventType, clazz);
            } catch (Exception e) {
                throw new RuntimeException("Cannot instantiate eventing class: " + clazz, e);
            }
        }
    }

    public Class<? extends Event> getEventClass(EventType type) {
        return eventTypeToClass.get(type);
    }

    public Class<? extends Event> getEventClass(String type) {
         EventType eventType = eventTypeToClass
                 .keySet()
                 .stream()
                 .filter(event -> Objects.equals(event.getType(), type))
                 .findFirst()
                 .orElseThrow(() -> new IllegalArgumentException("Unknown eventing type: " + type));
         return getEventClass(eventType);
    }

    public List<Class<? extends Event>> getEventClasses() {
        return new ArrayList<>(eventTypeToClass.values());
    }

    public boolean hasType(EventType type) {
        return eventTypeToClass.containsKey(type);
    }

    public boolean hasType(String type) {
        return eventTypeToClass.keySet().stream().map(EventType::getType).anyMatch(t -> t.equals(type));
    }
}
