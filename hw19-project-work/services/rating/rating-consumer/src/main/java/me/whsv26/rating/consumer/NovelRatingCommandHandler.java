package me.whsv26.rating.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.whsv26.rating.model.NovelRatingCommand;
import me.whsv26.rating.model.NovelRatingEvent;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class NovelRatingCommandHandler {

    private final StringRedisTemplate redisTemplate;

    private final KafkaProps kafkaProps;

    private final KafkaTemplate<String, NovelRatingEvent> kafkaTemplate;

    public void handleIdempotent(NovelRatingCommand command) {
        var commandTopic = kafkaProps.consumer().ratingCommand().topic();
        var idempotencyKeyTtl = kafkaProps.consumer().ratingCommand().idempotencyKeyTtl();
        var idempotencyKey = "%s:%s".formatted(commandTopic, command.commandId());
        var counterKey = "novel:%s:rating-count".formatted(command.novelId());
        var totalKey = "novel:%s:total-rating".formatted(command.novelId());
        var session = makeSession(command, idempotencyKey, counterKey, totalKey, idempotencyKeyTtl);
        var isSkipped = redisTemplate.execute(session);

        if (Boolean.TRUE.equals(isSkipped)) {
            log.warn("Duplicated command detected, skipping redis ops: {}", command.commandId());
        }

        sendEvent(command.commandId(), command.novelId(), counterKey, totalKey);
    }

    private SessionCallback<Object> makeSession(
        NovelRatingCommand command,
        String idempotencyKey,
        String counterKey,
        String totalKey,
        Duration idempotencyKeyTtl
    ) {
        return new SessionCallback<>() {
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
                operations.opsForValue().set((K) idempotencyKey, (V) "1", idempotencyKeyTtl);
                return operations.exec().isEmpty();
            }
        };
    }

    private void sendEvent(String eventId, String novelId, String counterKey, String totalKey) {
        var eventTopic = kafkaProps.producer().ratingEvent().topic();
        var avgRating = calculateAvgRating(counterKey, totalKey);
        var event = new NovelRatingEvent(eventId, novelId, avgRating.floatValue());
        kafkaTemplate.send(eventTopic, event);
    }

    private BigDecimal calculateAvgRating(String counterKey, String totalKey) {
        var maybeCounter = Optional.ofNullable(redisTemplate.opsForValue().get(counterKey))
            .flatMap(this::parseBigDecimal);
        var maybeTotal = Optional.ofNullable(redisTemplate.opsForValue().get(totalKey))
            .flatMap(this::parseBigDecimal);
        return maybeTotal
            .flatMap(total -> maybeCounter.map(total::divide))
            .orElse(BigDecimal.ZERO);
    }

    private Optional<BigDecimal> parseBigDecimal(String x) {
        try {
            return Optional.of(BigDecimal.valueOf(Long.parseLong(x)));
        } catch (NumberFormatException err) {
            return Optional.empty();
        }
    }
}
