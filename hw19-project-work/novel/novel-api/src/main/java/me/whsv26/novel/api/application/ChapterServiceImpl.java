package me.whsv26.novel.api.application;

import lombok.RequiredArgsConstructor;
import me.whsv26.novel.api.domain.Chapter;
import me.whsv26.novel.api.domain.ChapterId;
import me.whsv26.novel.api.domain.NovelId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {

    private final ChapterRepository chapterRepository;

    private final Clock clock;

    @Override
    public Page<ChapterPreview> findByNovelId(NovelId novelId, Pageable pageable) {
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
            novelId,
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

    @Override
    public void deleteByNovelId(NovelId novelId) {
        var chapters = chapterRepository.findAllByNovelId(novelId);
        var chapterIds = chapters.stream().map(ChapterPreview::id).toList();
        chapterRepository.deleteAllById(chapterIds);
    }

    private Chapter findChapterById(ChapterId id) {
        return chapterRepository.findById(id)
            .orElseThrow(() -> new ChapterNotFoundException(id));
    }
}
