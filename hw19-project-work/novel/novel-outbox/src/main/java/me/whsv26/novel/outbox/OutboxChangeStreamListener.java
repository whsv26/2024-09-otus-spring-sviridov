package me.whsv26.novel.outbox;

import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.bson.Document;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxChangeStreamListener implements CommandLineRunner {

    public static final String COLLECTION_NAME = "outbox";

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

        mongoTemplate.getCollection(COLLECTION_NAME)
            .watch(pipeline)
            .forEach(this::handleOutboxedMessage);
    }

    private void handleOutboxedMessage(ChangeStreamDocument<Document> change) {
        Optional.ofNullable(change.getFullDocument())
            .filter(doc -> !doc.getBoolean("processed"))
            .ifPresent(doc -> {
                publish(doc);
                markAsProcessed(doc);
            });
    }

    private void markAsProcessed(Document fullDocument) {
        var documentId = fullDocument.getString("_id");
        var query = Query.query(Criteria.where("_id").is(documentId));
        var update = new Update().set("processed", true);
        mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
    }

    private void publish(Document fullDocument) {
        var payload = fullDocument.getString("payload");
        var typeId = fullDocument.getString("messageType");
        var record = new ProducerRecord<String, String>(kafkaConfig.getTopic(), payload);
        record.headers().add("__TypeId__", typeId.getBytes(StandardCharsets.UTF_8));
        kafkaTemplate.send(record);
        log.debug("Published outbox message: {}", payload);
    }
}
