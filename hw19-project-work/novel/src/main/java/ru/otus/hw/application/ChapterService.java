package ru.otus.hw.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.hw.domain.Chapter;
import ru.otus.hw.domain.ChapterId;
import ru.otus.hw.domain.NovelId;

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
