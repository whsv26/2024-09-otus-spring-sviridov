package me.whsv26.libs.outbox.mongo;

import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.core.KafkaTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static me.whsv26.libs.outbox.mongo.OutboxMessageField.FIELD_ID;
import static me.whsv26.libs.outbox.mongo.OutboxMessageField.FIELD_MESSAGE_TYPE;
import static me.whsv26.libs.outbox.mongo.OutboxMessageField.FIELD_OPERATION_TYPE;
import static me.whsv26.libs.outbox.mongo.OutboxMessageField.FIELD_PAYLOAD;
import static me.whsv26.libs.outbox.mongo.OutboxMessageField.FIELD_PROCESSED;
import static me.whsv26.libs.outbox.mongo.OutboxMessageField.FIELD_TOPIC;

@Slf4j
@RequiredArgsConstructor
public class OutboxChangeStreamWatcher {

    public static final String COLLECTION_NAME = "outbox";

    public static final String HEADER_TYPE_ID = "__TypeId__";

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final MongoTemplate mongoTemplate;

    public void watchForChanges() {
        var filter = Filters.eq(FIELD_OPERATION_TYPE, "insert");
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
