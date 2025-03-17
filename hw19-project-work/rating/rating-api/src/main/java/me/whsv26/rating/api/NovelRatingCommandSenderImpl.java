package me.whsv26.rating.api;

import me.whsv26.rating.model.NovelRatingCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class NovelRatingCommandSenderImpl implements NovelRatingCommandSender {

    private final String topic;

    private final KafkaTemplate<String, NovelRatingCommand> kafkaTemplate;

    public NovelRatingCommandSenderImpl(
        @Value("${application.kafka.topic}")
        String topic,
        KafkaTemplate<String, NovelRatingCommand> kafkaTemplate
    ) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(NovelRatingCommand command) {
        kafkaTemplate.send(topic, command);
    }
}
