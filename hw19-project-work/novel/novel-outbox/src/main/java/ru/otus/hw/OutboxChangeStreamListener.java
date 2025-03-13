package ru.otus.hw;

import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxChangeStreamListener implements CommandLineRunner {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final MongoTemplate mongoTemplate;

    private final KafkaConfig kafkaConfig;

    @Override
    public void run(String... args) {
        startListening();
    }

    public void startListening() {
        var filter = Filters.eq("operationType", "insert");
        var matchStage = Aggregates.match(filter);
        var pipeline = List.of(matchStage);

        mongoTemplate.getCollection("outbox")
            .watch(pipeline)
            .forEach(this::produceEvent);
    }

    private void produceEvent(ChangeStreamDocument<Document> change) {
        var fullDocument = change.getFullDocument();
        var payload = fullDocument.getString("payload"); // TODO

        kafkaTemplate.send(kafkaConfig.getTopic(), payload);

        log.info("Published outbox event: {}", payload);
    }
}
