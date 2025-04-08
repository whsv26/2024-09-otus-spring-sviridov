package me.whsv26.libs.outbox.mongo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "outbox")
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OutboxMessage {

    @Id
    @Field(name = "_id")
    private String id;

    @Field(name = "messageType")
    private String messageType;

    @Field(name = "topic")
    private String topic;

    @Field(name = "payload")
    private String payload;

    @Field(name = "createdAt")
    private LocalDateTime createdAt;

    @Field(name = "processed")
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
