package me.whsv26.rating.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import me.whsv26.rating.model.NovelRatingCommand;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class RatingController {

    private final StringRedisTemplate redisTemplate;

    private final NovelRatingCommandSender novelRatingCommandSender;

    @GetMapping({"/api/novels/{novelId}", "/internal/novels/{novelId}"})
    public ReadNovelRatingResponse readNovelRating(
        @PathVariable("novelId")
        String novelId
    ) {
        var counterKey = "novel:%s:rating-count".formatted(novelId);
        var totalKey = "novel:%s:total-rating".formatted(novelId);
        var maybeCounter = Optional.ofNullable(redisTemplate.opsForValue().get(counterKey))
            .flatMap(RatingController::parseBigDecimal);
        var maybeTotal = Optional.ofNullable(redisTemplate.opsForValue().get(totalKey))
            .flatMap(RatingController::parseBigDecimal);
        var avgRating = maybeTotal.flatMap(total -> maybeCounter.map(total::divide));

        return new ReadNovelRatingResponse(avgRating.orElse(BigDecimal.ZERO));
    }

    @PostMapping("/api/novels/{novelId}/ratings")
    public void createNovelRating(
        @RequestHeader("X-User-ID")
        String userId,
        @RequestHeader(value = "X-Idempotency-Key", required = false)
        String idempotencyKey,
        @PathVariable("novelId")
        String novelId,
        @RequestBody
        @Valid
        CreateNovelRatingRequest request
    ) {
        var commandId = Optional.ofNullable(idempotencyKey)
            .orElseGet(() -> UUID.randomUUID().toString());
        var command = new NovelRatingCommand(commandId, novelId, userId, request.rating);
        novelRatingCommandSender.send(command);
    }

    public record CreateNovelRatingRequest(
        @NotNull
        @Min(1)
        @Max(10)
        int rating
    ) {}

    public record ReadNovelRatingResponse(
        BigDecimal avgRating
    ) {}

    private static Optional<BigDecimal> parseBigDecimal(String x) {
        try {
            return Optional.of(BigDecimal.valueOf(Long.parseLong(x)));
        } catch (NumberFormatException err) {
            return Optional.empty();
        }
    }
}
