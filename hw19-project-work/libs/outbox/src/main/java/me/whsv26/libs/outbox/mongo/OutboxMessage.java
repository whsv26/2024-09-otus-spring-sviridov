package me.whsv26.libs.outbox.mongo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

import static me.whsv26.libs.outbox.mongo.OutboxMessageField.FIELD_CREATED_AT;
import static me.whsv26.libs.outbox.mongo.OutboxMessageField.FIELD_ID;
import static me.whsv26.libs.outbox.mongo.OutboxMessageField.FIELD_MESSAGE_TYPE;
import static me.whsv26.libs.outbox.mongo.OutboxMessageField.FIELD_PAYLOAD;
import static me.whsv26.libs.outbox.mongo.OutboxMessageField.FIELD_PROCESSED;
import static me.whsv26.libs.outbox.mongo.OutboxMessageField.FIELD_TOPIC;

@Document(collection = "outbox")
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OutboxMessage {

    @Id
    @Field(name = FIELD_ID)
    private String id;

    @Field(name = FIELD_MESSAGE_TYPE)
    private String messageType;

    @Field(name = FIELD_TOPIC)
    private String topic;

    @Field(name = FIELD_PAYLOAD)
    private String payload;

    @Field(name = FIELD_CREATED_AT)
    private LocalDateTime createdAt;

    @Field(name = FIELD_PROCESSED)
    private boolean processed;

    public static OutboxMessage of(
        Class<?> messageClass,
        String messageId,
        String topic,
        String payload
    ) {
        var message = new OutboxMessage();
        message.id = messageId;
        message.messageType = messageClass.getName();
        message.topic = topic;
        message.createdAt = LocalDateTime.now();
        message.processed = false;
        message.payload = payload;

        return message;
    }
}
