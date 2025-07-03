package com.faceit.userservice.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaPublisher {
    private final KafkaTemplate<String, UserChangedEvent> userChangedEventKafkaTemplate;
    private final KafkaPublisherProperties properties;

    @Async
    public void publish(UserChangedEvent event) {
        userChangedEventKafkaTemplate.send(properties.getUserEventsTopic(), event.getUser().getId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish event: {}", ex.getMessage(), ex);
                    } else {
                        log.info("Event published!");
                    }
                });
    }
}
