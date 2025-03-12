package ru.otus.hw.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Chapter;
import ru.otus.hw.domain.ChapterId;
import ru.otus.hw.domain.NovelId;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {

    private final ChapterRepository chapterRepository;

    private final Clock clock;

    @Override
    public Page<Chapter> findByNovelId(NovelId novelId, Pageable pageable) {
        return chapterRepository.findByNovelId(novelId, pageable);
    }

    @Override
    public Chapter findById(ChapterId id) {
        return findChapterById(id);
    }

    @Override
    public Chapter create(NovelId novelId, String title, String content) {
        var chapter = new Chapter(
            ChapterId.next(),
            title,
            content,
            clock
        );
        return chapterRepository.save(chapter);
    }

    @Override
    public Chapter update(ChapterId id, String title, String content) {
        var chapter = findChapterById(id);
        chapter.setTitle(title);
        chapter.setContent(content);
        return chapterRepository.save(chapter);
    }

    @Override
    public void delete(ChapterId id) {
        chapterRepository.deleteById(id);
    }

    private Chapter findChapterById(ChapterId id) {
        return chapterRepository.findById(id)
            .orElseThrow(() -> new ChapterNotFoundException(id));
    }
}
