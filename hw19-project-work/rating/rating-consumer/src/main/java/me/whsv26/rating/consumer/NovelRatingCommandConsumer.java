package me.whsv26.rating.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.whsv26.rating.model.NovelRatingCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.lang.NonNull;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class NovelRatingCommandConsumer {

    private final StringRedisTemplate redisTemplate;

    @Value("${application.kafka.ttl}")
    private final Duration ttl;

    @Value("${application.kafka.topic}")
    private final String topic;

    @KafkaListener(
        topics = "${application.kafka.novel.topic}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeMessage(@Payload NovelRatingCommand command) {
        var idempotencyKey = "%s:%s".formatted(topic, command.commandId());
        var counterKey = "novel:%s:rating-count".formatted(command.novelId());
        var totalKey = "novel:%s:total-rating".formatted(command.novelId());

        var isSkipped = redisTemplate.execute(new SessionCallback<>() {
            @SuppressWarnings("unchecked")
            @Override
            public <K, V> Boolean execute(@NonNull RedisOperations<K, V> operations) {
                operations.watch((K) idempotencyKey);
                if (operations.opsForValue().get(idempotencyKey) != null) {
                    operations.unwatch();
                    return true;
                }
                operations.multi();
                operations.opsForValue().increment((K) counterKey);
                operations.opsForValue().increment((K) totalKey, command.rating());
                operations.opsForValue().set((K) idempotencyKey, (V) "1", ttl);
                return operations.exec().isEmpty();
            }
        });

        if (Boolean.TRUE.equals(isSkipped)) {
            log.warn("Duplicated command has been skipped: {}", command.commandId());
        }
    }
}