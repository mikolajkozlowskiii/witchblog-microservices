package org.common.eventing.core.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.common.eventing.core.model.Event;
import org.common.eventing.core.registry.EventClassRegistry;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EventDeserializer extends JsonDeserializer<Event> {

    private final EventClassRegistry registry;

    public EventDeserializer(EventClassRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Event deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);

        String type = node.get("type").get("type").asText();
        Class<? extends Event> clazz = registry.getEventClass(type);

        if (clazz == null) {
            throw new IllegalArgumentException("Unknown eventing type: " + type);
        }

        return mapper.treeToValue(node, clazz);
    }
}
