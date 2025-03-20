package me.whsv26.novel.api.infrastructure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "application.kafka")
public record KafkaProps(ProducerProps producer) {

    public record ProducerProps(NovelEventProps novelEvent) {

        public record NovelEventProps(String topic) {}
    }
}
