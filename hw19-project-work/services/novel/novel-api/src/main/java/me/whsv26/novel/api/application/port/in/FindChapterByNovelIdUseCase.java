package me.whsv26.novel.api.application.port.in;

import me.whsv26.novel.api.application.dto.ChapterPreview;
import me.whsv26.novel.api.application.dto.PagedResult;
import me.whsv26.novel.api.domain.valueobject.NovelId;

@FunctionalInterface
public interface FindChapterByNovelIdUseCase {

    PagedResult<ChapterPreview> findByNovelId(NovelId novelId, int page, int size);
}
