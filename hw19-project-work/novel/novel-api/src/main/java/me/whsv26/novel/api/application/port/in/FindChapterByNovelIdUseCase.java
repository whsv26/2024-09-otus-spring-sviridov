package me.whsv26.novel.api.application.port.in;

import me.whsv26.novel.api.application.ChapterPreview;
import me.whsv26.novel.api.domain.NovelId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindChapterByNovelIdUseCase {
    Page<ChapterPreview> findByNovelId(NovelId novelId, Pageable pageable);
}
