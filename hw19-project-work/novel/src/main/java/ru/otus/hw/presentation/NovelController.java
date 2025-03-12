package ru.otus.hw.presentation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.application.NovelService;
import ru.otus.hw.domain.AuthorId;
import ru.otus.hw.domain.GenreId;
import ru.otus.hw.domain.Novel;
import ru.otus.hw.domain.ValueObject;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NovelController {

    private final NovelService userService;

    private final NovelMapper novelMapper = Mappers.getMapper(NovelMapper.class);

    @PostMapping("/users")
    public CreateResponse create(@RequestBody @Valid NovelController.CreateRequest request) {
        var novel = userService.create(
            new AuthorId(request.authorId),
            request.title,
            request.synopsis,
            request.genres.stream().map(GenreId::new).toList(),
            request.tags
        );
        return new CreateResponse(novelMapper.map(novel));
    }

    public record CreateRequest(
        @NotBlank
        String authorId,
        @NotBlank
        String title,
        @NotNull
        String synopsis,
        @NotNull
        @Size(min = 1)
        List<@NotBlank String> genres,
        @NotNull
        List<@NotBlank String> tags
    ) {}

    public record CreateResponse(NovelResponse novel) {}

    public record NovelResponse(
        String id,
        String authorId,
        String title,
        String synopsis,
        List<String> genres,
        List<String> tags,
        String createdAt
    ) {}

    @Mapper
    interface NovelMapper {

        NovelResponse map(Novel source);

        default String map(ValueObject<String> source) {
            return source.value();
        }
    }
}
