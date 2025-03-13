package ru.otus.hw.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.domain.Chapter;
import ru.otus.hw.domain.ChapterId;
import ru.otus.hw.domain.NovelId;

import java.util.List;

public interface ChapterRepository extends MongoRepository<Chapter, ChapterId> {

    Page<ChapterPreview> findByNovelId(NovelId novelId, Pageable pageable);

    List<ChapterPreview> findAllByNovelId(NovelId novelId);

    default void deleteByNovelId(NovelId novelId) {
        var chapters = findAllByNovelId(novelId);
        var chapterIds = chapters.stream().map(ChapterPreview::id).toList();
        deleteAllById(chapterIds);
    }
}
