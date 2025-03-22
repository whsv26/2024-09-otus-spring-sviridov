package me.whsv26.novel.api.application.port.out;

import me.whsv26.novel.api.application.dto.ChapterPreview;
import me.whsv26.novel.api.application.dto.PagedResult;
import me.whsv26.novel.api.domain.entity.Chapter;
import me.whsv26.novel.api.domain.valueobject.ChapterId;
import me.whsv26.novel.api.domain.valueobject.NovelId;

import java.util.List;
import java.util.Optional;

public interface ChapterRepository {

    PagedResult<ChapterPreview> findByNovelId(NovelId novelId, int page, int size);

    List<ChapterPreview> findAllByNovelId(NovelId novelId);

    Optional<Chapter> findById(ChapterId id);

    Chapter save(Chapter entity);

    void deleteById(ChapterId id);

    void deleteAllById(List<ChapterId> ids);
}
