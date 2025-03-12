package ru.otus.hw.presentation;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.application.ChapterService;
import ru.otus.hw.domain.Chapter;
import ru.otus.hw.domain.NovelId;
import ru.otus.hw.domain.ValueObject;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChapterController {


    private final ChapterMapper mapper = Mappers.getMapper(ChapterMapper.class);

    private final ChapterService chapterService;

    @GetMapping("/novels/{id}/chapters")
    public ListNovelChaptersResponse listNovelChapters(
        @PathVariable("id")
        String novelId,
        Pageable pageable
    ) {
        var page = chapterService.findByNovelId(new NovelId(novelId), pageable);
        return new ListNovelChaptersResponse(
            page.stream().map(mapper::map).toList(),
            mapper.map(page)
        );
    }

    public record ListNovelChaptersResponse(
        List<ChapterResponse> chapters,
        PageMeta meta
    ) {}

    public record ChapterResponse(
        String id,
        String title,
        String content
    ) {}

    @Mapper
    interface ChapterMapper {

        ChapterResponse map(Chapter source);

        default String map(ValueObject<String> source) {
            return source.value();
        }

        @Mapping(source = "number", target = "page")
        @Mapping(expression = "java(source.hasNext())", target = "hasNext")
        PageMeta map(Page<?> source);
    }
}
