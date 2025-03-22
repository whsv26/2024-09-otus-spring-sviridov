package me.whsv26.novel.api.presentation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import me.whsv26.novel.api.application.ChapterPreview;
import me.whsv26.novel.api.application.port.in.ChapterUseCases;
import me.whsv26.novel.api.domain.Chapter;
import me.whsv26.novel.api.domain.ChapterId;
import me.whsv26.novel.api.domain.NovelId;
import me.whsv26.novel.api.domain.ValueObject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChapterController {

    private final ChapterMapper mapper = Mappers.getMapper(ChapterMapper.class);

    private final ChapterUseCases chapterService;

    @GetMapping("/api/novels/{novelId}/chapters")
    public ListNovelChaptersResponse listNovelChapters(
        @PathVariable("novelId")
        String novelId,
        Pageable pageable
    ) {
        var page = chapterService.findByNovelId(new NovelId(novelId), pageable);
        return new ListNovelChaptersResponse(
            page.stream().map(mapper::map).toList(),
            mapper.map(page)
        );
    }

    @GetMapping("/api/novels/{novelId}/chapters/{chapterId}")
    public ReadNovelChapterResponse readNovelChapter(
        @PathVariable("novelId")
        String novelId,
        @PathVariable("chapterId")
        String chapterId,
        Pageable pageable
    ) {
        var chapter = chapterService.findById(new ChapterId(chapterId));
        return new ReadNovelChapterResponse(mapper.map(chapter));
    }

    @PostMapping("/api/novels/{novelId}/chapters")
    public CreateNovelChapterResponse createNovelChapter(
        @PathVariable("novelId")
        String novelId,
        @RequestBody
        @Valid
        CreateNovelChapterRequest request
    ) {
        var chapter = chapterService.create(
            new NovelId(novelId),
            request.title,
            request.content
        );
        return new CreateNovelChapterResponse(mapper.map(chapter));
    }

    @PutMapping("/api/novels/{novelId}/chapters/{chapterId}")
    public UpdateNovelChapterResponse updateNovelChapter(
        @PathVariable("novelId")
        String novelId,
        @PathVariable("chapterId")
        String chapterId,
        @RequestBody
        @Valid
        UpdateNovelChapterRequest request
    ) {
        var chapter = chapterService.update(
            new ChapterId(chapterId),
            request.title,
            request.content
        );
        return new UpdateNovelChapterResponse(mapper.map(chapter));
    }

    @DeleteMapping("/api/novels/{novelId}/chapters/{chapterId}")
    public void deleteNovelChapter(
        @PathVariable("novelId")
        String novelId,
        @PathVariable("chapterId")
        String chapterId
    ) {
        chapterService.delete(new ChapterId(chapterId));
    }
    
    public record CreateNovelChapterRequest(
        @NotBlank String title,
        @NotBlank String content
    ) {}
    
    public record CreateNovelChapterResponse(
        ChapterResponse chapter
    ) {}

    public record UpdateNovelChapterRequest(
        @NotBlank String title,
        @NotBlank String content
    ) {}

    public record UpdateNovelChapterResponse(
        ChapterResponse chapter
    ) {}

    public record ListNovelChaptersResponse(
        List<ChapterPreviewResponse> chapters,
        PageMeta meta
    ) {}

    public record ReadNovelChapterResponse(
        ChapterResponse chapter
    ) {}

    public record ChapterPreviewResponse(
        String id,
        String title
    ) {}

    public record ChapterResponse(
        String id,
        String title,
        String content,
        LocalDateTime createdAt
    ) {}

    @Mapper
    interface ChapterMapper {

        ChapterResponse map(Chapter source);

        ChapterPreviewResponse map(ChapterPreview source);

        default String map(ValueObject<String> source) {
            return source.value();
        }

        @Mapping(source = "number", target = "page")
        @Mapping(expression = "java(source.hasNext())", target = "hasNext")
        PageMeta map(Page<?> source);
    }
}
