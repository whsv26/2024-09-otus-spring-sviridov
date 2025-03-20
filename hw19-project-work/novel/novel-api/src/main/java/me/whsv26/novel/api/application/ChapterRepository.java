package me.whsv26.novel.api.application;

import me.whsv26.novel.api.domain.Chapter;
import me.whsv26.novel.api.domain.ChapterId;
import me.whsv26.novel.api.domain.NovelId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChapterRepository extends MongoRepository<Chapter, ChapterId> {

    Page<ChapterPreview> findByNovelId(NovelId novelId, Pageable pageable);

    List<ChapterPreview> findAllByNovelId(NovelId novelId);
}
