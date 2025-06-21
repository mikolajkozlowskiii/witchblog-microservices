package org.common.eventing.core.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.common.eventing.core.model.Event;
import org.common.eventing.core.serializer.EventDeserializer;
import org.common.eventing.core.registry.EventClassRegistry;
import org.common.eventing.core.serializer.EventSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaEventConfig {

    private final String bootstrapServers;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final EventDeserializer deserializer;
    private final EventClassRegistry registry;

    public KafkaEventConfig(@Value("${kafka.bootstrap-servers:localhost:9092}") String bootstrapServers, EventDeserializer deserializer, EventClassRegistry registry) {
        this.bootstrapServers = bootstrapServers;
        this.deserializer = deserializer;
        this.registry = registry;
    }


    @Bean
    public ProducerFactory<String, Event> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        return new DefaultKafkaProducerFactory<>(config, new StringSerializer(), new EventSerializer());
    }

    @Bean
    public KafkaTemplate<String, Event> kafkaTemplate(ProducerFactory<String, Event> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ConsumerFactory<String, Event> consumerFactory() {
        registry.getEventClasses().forEach(objectMapper::registerSubtypes);

        SimpleModule module = new SimpleModule();
        module.addDeserializer(Event.class, deserializer);
        objectMapper.registerModule(module);
        JsonDeserializer<Event> jsonDeserializer = new JsonDeserializer<>(Event.class, objectMapper);
        jsonDeserializer.addTrustedPackages("*");

        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Event> kafkaListenerContainerFactory(
            ConsumerFactory<String, Event> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, Event> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}

