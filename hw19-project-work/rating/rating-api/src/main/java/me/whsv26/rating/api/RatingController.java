package me.whsv26.rating.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import me.whsv26.rating.model.NovelRatingCommand;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class RatingController {

//    private final RatingService ratingService;

    private final NovelRatingCommandSender novelRatingCommandSender;

    private final RatingMapper mapper = Mappers.getMapper(RatingMapper.class);

//    @GetMapping("/novels/{id}")
//    public ReadNovelResponse readNovelRating(
//        @PathVariable("id")
//        String novelId
//    ) {
//        var novel = novelService.findById(new NovelId(novelId));
//        return new ReadNovelResponse(mapper.map(novel));
//    }

    @PostMapping("/novels/{novelId}/ratings")
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
        Integer rating
    ) {}

    @Mapper
    interface RatingMapper {

//        NovelResponse map(Novel source);

//        default String map(ValueObject<String> source) {
//            return source.value();
//        }
    }
}
