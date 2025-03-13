package ru.otus.hw;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "application.kafka")
public final class AppProperties implements KafkaConfig {

    private String topic;
}
