package me.whsv26.search.api;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NovelSearchController {

    private final NovelRepository novelRepository;

    private final NovelMapper novelMapper = Mappers.getMapper(NovelMapper.class);

    @GetMapping("/api/novels")
    public SearchNovelsResponse searchNovels(SearchNovelsRequest request, Pageable pageable) {
        var searchParams = novelMapper.map(request);
        var page = novelRepository.search(searchParams, pageable);
        return new SearchNovelsResponse(
            page.map(novelMapper::map).toList(),
            novelMapper.map(page)
        );
    }

    public record SearchNovelsRequest(
        String prompt,
        String authorName,
        Float ratingFrom,
        Float ratingTo,
        List<String> genres,
        List<String> tags
    ) {}

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

        @Mapping(target = "ratingRange", expression = "java(map(source.ratingFrom(), source.ratingTo()))")
        NovelSearchParams map(SearchNovelsRequest source);

        NovelResponse map(Novel source);

        @Mapping(source = "number", target = "page")
        @Mapping(expression = "java(source.hasNext())", target = "hasNext")
        PageMeta map(Page<?> source);

        default Range<Float> map(Float ratingFrom, Float ratingTo) {
            return Range.of(ratingFrom, ratingTo)
                .orElse(null);
        }
    }
}
