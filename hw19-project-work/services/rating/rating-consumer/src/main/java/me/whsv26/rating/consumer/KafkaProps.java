package me.whsv26.rating.consumer;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "application.kafka")
public record KafkaProps(ConsumerProps consumer, ProducerProps producer) {

    public record ConsumerProps(RatingCommandProps ratingCommand) {

        public record RatingCommandProps(String topic, Duration idempotencyKeyTtl) {}
    }

    public record ProducerProps(RatingEventProps ratingEvent) {

        public record RatingEventProps(String topic) {}
    }
}
