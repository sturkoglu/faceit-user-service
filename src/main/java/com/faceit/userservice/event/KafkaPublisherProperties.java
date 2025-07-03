package com.faceit.userservice.event;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "kafka.publisher")
public class KafkaPublisherProperties {
    private String userEventsTopic;
}