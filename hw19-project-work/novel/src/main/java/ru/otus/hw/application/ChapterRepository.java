package ru.otus.hw.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.domain.AuthorId;
import ru.otus.hw.domain.Chapter;
import ru.otus.hw.domain.ChapterId;
import ru.otus.hw.domain.Novel;
import ru.otus.hw.domain.NovelId;

import java.util.List;

public interface ChapterRepository extends MongoRepository<Chapter, ChapterId> {

    Page<Chapter> findByNovelId(NovelId novelId, Pageable pageable);
}
