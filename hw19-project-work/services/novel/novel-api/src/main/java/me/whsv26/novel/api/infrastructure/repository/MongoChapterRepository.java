package me.whsv26.novel.api.infrastructure.repository;

import me.whsv26.novel.api.application.dto.ChapterPreview;
import me.whsv26.novel.api.domain.entity.Chapter;
import me.whsv26.novel.api.domain.valueobject.ChapterId;
import me.whsv26.novel.api.domain.valueobject.NovelId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoChapterRepository extends MongoRepository<Chapter, ChapterId> {

    Page<ChapterPreview> findByNovelId(NovelId novelId, Pageable pageable);

    List<ChapterPreview> findAllByNovelId(NovelId novelId);
}
