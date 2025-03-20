package me.whsv26.novel.api.application;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

// TODO move to lib
@Document(collection = "outbox")
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OutboxMessage {

    @Id
    private String id;

    private String messageType;

    private String topic;

    private String payload;

    private LocalDateTime createdAt;

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
