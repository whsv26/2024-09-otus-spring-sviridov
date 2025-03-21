package me.whsv26.novel.api.presentation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import me.whsv26.novel.api.application.NovelService;
import me.whsv26.novel.api.domain.AuthorId;
import me.whsv26.novel.api.domain.GenreId;
import me.whsv26.novel.api.domain.Novel;
import me.whsv26.novel.api.domain.NovelId;
import me.whsv26.novel.api.domain.ValueObject;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NovelController {

    private final NovelService novelService;

    private final NovelMapper mapper = Mappers.getMapper(NovelMapper.class);

    @GetMapping("/api/novels/{id}")
    public ReadNovelResponse readNovel(
        @PathVariable("id")
        String novelId
    ) {
        var novel = novelService.findById(new NovelId(novelId));
        return new ReadNovelResponse(mapper.map(novel));
    }

    @PostMapping("/api/novels")
    public CreateNovelResponse createNovel(
        @RequestHeader("X-User-ID")
        String userId,
        @RequestBody
        @Valid
        CreateNovelRequest request
    ) {
        var novel = novelService.create(
            new AuthorId(userId),
            request.title,
            request.synopsis,
            request.genres.stream().map(GenreId::new).toList(),
            request.tags
        );
        return new CreateNovelResponse(mapper.map(novel));
    }

    @PutMapping("/api/novels/{id}")
    public UpdateNovelResponse updateNovel(
        @PathVariable("id")
        String novelId,
        @RequestBody
        @Valid
        UpdateNovelRequest request
    ) {
        // TODO allow update only for own novels
        var novel = novelService.update(
            new NovelId(novelId),
            request.title,
            request.synopsis,
            request.genres.stream().map(GenreId::new).toList(),
            request.tags
        );
        return new UpdateNovelResponse(mapper.map(novel));
    }

    @DeleteMapping("/api/novels/{id}")
    public void deleteNovel(@PathVariable("id") String novelId) {
        // TODO allow delete only for own novels
        novelService.delete(new NovelId(novelId));
    }

    public record CreateNovelRequest(
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

    public record CreateNovelResponse(NovelResponse novel) {}

    public record UpdateNovelRequest(
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

    public record ReadNovelResponse(NovelResponse novel) {}

    public record UpdateNovelResponse(NovelResponse novel) {}

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
