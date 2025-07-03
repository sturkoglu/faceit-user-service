package com.faceit.userservice.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserEventKafkaPublisher {
    private final KafkaTemplate<String, UserChangedEvent> kafkaTemplate;

    public UserEventKafkaPublisher(KafkaTemplate<String, UserChangedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(UserChangedEvent event) {
        kafkaTemplate.send("user-events", event.getUser().getId().toString(), event);
    }
}
