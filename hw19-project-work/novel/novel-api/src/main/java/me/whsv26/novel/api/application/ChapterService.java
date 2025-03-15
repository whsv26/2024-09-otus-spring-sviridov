package me.whsv26.novel.api.application;

import me.whsv26.novel.api.domain.Chapter;
import me.whsv26.novel.api.domain.ChapterId;
import me.whsv26.novel.api.domain.NovelId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChapterService {

    Page<ChapterPreview> findByNovelId(NovelId novelId, Pageable pageable);

    Chapter findById(ChapterId id);

    Chapter create(
        NovelId novelId,
        String title,
        String content
    );

    Chapter update(
        ChapterId id,
        String title,
        String content
    );

    void delete(ChapterId id);
}
