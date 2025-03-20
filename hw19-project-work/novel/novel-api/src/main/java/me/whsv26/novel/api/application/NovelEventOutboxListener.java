package me.whsv26.novel.api.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.whsv26.novel.api.infrastructure.KafkaProps;
import me.whsv26.novel.model.NovelCreatedEvent;
import me.whsv26.novel.model.NovelDeletedEvent;
import me.whsv26.novel.model.NovelUpdatedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NovelEventOutboxListener {

    private final OutboxEventRepository outboxEventRepository;

    private final ObjectMapper objectMapper;

    private final KafkaProps kafkaProps;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(NovelCreatedEvent event) throws JsonProcessingException {
        var payload = objectMapper.writeValueAsString(event);
        var topic = kafkaProps.producer().novelEvent().topic();
        var outboxMessage = OutboxMessage.of(NovelCreatedEvent.class, event.eventId(), topic, payload);
        outboxEventRepository.save(outboxMessage);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(NovelUpdatedEvent event) throws JsonProcessingException {
        var payload = objectMapper.writeValueAsString(event);
        var topic = kafkaProps.producer().novelEvent().topic();
        var outboxMessage = OutboxMessage.of(NovelUpdatedEvent.class, event.eventId(), topic, payload);
        outboxEventRepository.save(outboxMessage);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(NovelDeletedEvent event) throws JsonProcessingException {
        var payload = objectMapper.writeValueAsString(event);
        var topic = kafkaProps.producer().novelEvent().topic();
        var outboxMessage = OutboxMessage.of(NovelDeletedEvent.class, event.eventId(), topic, payload);
        outboxEventRepository.save(outboxMessage);
    }
}
