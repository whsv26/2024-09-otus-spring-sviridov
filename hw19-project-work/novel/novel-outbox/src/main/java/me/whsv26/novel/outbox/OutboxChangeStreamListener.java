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

    public static final String HEADER_TYPE_ID = "__TypeId__";

    public static final String FIELD_ID = "_id";

    public static final String FIELD_PROCESSED = "processed";

    public static final String FIELD_PAYLOAD = "payload";

    public static final String FIELD_MESSAGE_TYPE = "messageType";

    public static final String FIELD_TOPIC = "topic";

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final MongoTemplate mongoTemplate;

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
            .filter(doc -> !doc.getBoolean(FIELD_PROCESSED))
            .ifPresent(doc -> {
                publish(doc);
                markAsProcessed(doc);
            });
    }

    private void markAsProcessed(Document fullDocument) {
        var documentId = fullDocument.getString(FIELD_ID);
        var query = Query.query(Criteria.where(FIELD_ID).is(documentId));
        var update = new Update().set(FIELD_PROCESSED, true);
        mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
    }

    private void publish(Document fullDocument) {
        var payload = fullDocument.getString(FIELD_PAYLOAD);
        var record = buildProducerRecord(fullDocument, payload);
        kafkaTemplate.send(record);
        log.debug("Published outbox message: {}", payload);
    }

    private static ProducerRecord<String, String> buildProducerRecord(Document fullDocument, String payload) {
        var typeId = fullDocument.getString(FIELD_MESSAGE_TYPE);
        var topic = fullDocument.getString(FIELD_TOPIC);
        var record = new ProducerRecord<String, String>(topic, payload);
        record.headers().add(HEADER_TYPE_ID, typeId.getBytes(StandardCharsets.UTF_8));
        return record;
    }
}
