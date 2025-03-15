package me.whsv26.novel.api.application;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "outbox")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEvent {

    @Id
    private String id;

    private String aggregateType;

    private String aggregateId;

    private String eventType;

    private LocalDateTime createdAt;

    private String payload;

    private boolean processed;

}
