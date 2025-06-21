package org.common.eventing.core.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.eventing.core.dispatcher.EventDispatcher;
import org.common.eventing.core.model.Event;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class GenericKafkaConsumer {

    private final EventDispatcher eventDispatcher;

    @KafkaListener(topics = "${common.consumer.topic}", groupId = "${common.consumer.group-id}")
    public void handleEvent(Event event) {
        try {
            eventDispatcher.dispatch(event);
        } catch (Exception e) {
            log.error("Kafka dispatch error: {}", e.getMessage());
        }
    }
}
