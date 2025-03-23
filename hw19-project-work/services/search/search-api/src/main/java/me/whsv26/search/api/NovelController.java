package me.whsv26.search.api;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NovelController {

    private final NovelRepository novelRepository;

    private final NovelMapper novelMapper = Mappers.getMapper(NovelMapper.class);

    @GetMapping("/api/novels")
    public SearchNovelsResponse searchNovels(
        @RequestParam(name = "prompt", required = false) String prompt,
        @RequestParam(name = "authorName", required = false) String authorName,
        @RequestParam(name = "ratingFrom", required = false) Float ratingFrom,
        @RequestParam(name = "ratingTo", required = false) Float ratingTo,
        @RequestParam(name = "genres", required = false) List<String> genres,
        @RequestParam(name = "tags", required = false) List<String> tags,
        Pageable pageable
    ) {
        var page = novelRepository.search(
            prompt,
            authorName,
            Range.of(ratingFrom, ratingTo).orElse(null),
            genres,
            tags,
            pageable
        );

        return new SearchNovelsResponse(
            page.map(novelMapper::map).toList(),
            novelMapper.map(page)
        );
    }

    public record SearchNovelsResponse(
        List<NovelResponse> novels,
        PageMeta meta
    ) {}

    public record NovelResponse(
        String id,
        float rating,
        String title,
        String synopsis,
        String authorId,
        String authorName,
        List<String> genres,
        List<String> tags
    ) {}

    @Mapper
    interface NovelMapper {

        NovelResponse map(Novel source);

        @Mapping(source = "number", target = "page")
        @Mapping(expression = "java(source.hasNext())", target = "hasNext")
        PageMeta map(Page<?> source);
    }
}
