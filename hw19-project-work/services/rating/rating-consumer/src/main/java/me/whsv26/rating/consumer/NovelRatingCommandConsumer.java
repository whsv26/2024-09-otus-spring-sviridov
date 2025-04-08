package me.whsv26.rating.consumer;

import lombok.RequiredArgsConstructor;
import me.whsv26.rating.model.NovelRatingCommand;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NovelRatingCommandConsumer {

    private final NovelRatingCommandHandler handler;

    @KafkaListener(
        topics = "${application.kafka.consumer.rating-command.topic}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeMessage(@Payload NovelRatingCommand command) {
        handler.handleIdempotent(command);
    }
}