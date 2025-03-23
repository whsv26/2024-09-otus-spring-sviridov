package me.whsv26.novel.api.application.service;

import lombok.RequiredArgsConstructor;
import me.whsv26.libs.auth.CurrentUser;
import me.whsv26.libs.auth.CurrentUserProvider;
import me.whsv26.novel.api.application.dto.ChapterPreview;
import me.whsv26.novel.api.application.dto.PagedResult;
import me.whsv26.novel.api.application.port.in.ChapterUseCases;
import me.whsv26.novel.api.application.port.out.ChapterRepository;
import me.whsv26.novel.api.application.port.out.NovelRepository;
import me.whsv26.novel.api.domain.exception.NovelNotFoundException;
import me.whsv26.novel.api.domain.entity.Chapter;
import me.whsv26.novel.api.domain.valueobject.ChapterId;
import me.whsv26.novel.api.domain.exception.ChapterNotFoundException;
import me.whsv26.novel.api.domain.entity.Novel;
import me.whsv26.novel.api.domain.valueobject.NovelId;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class ChapterService implements ChapterUseCases {

    private final NovelRepository novelRepository;

    private final ChapterRepository chapterRepository;

    private final CurrentUserProvider currentUserProvider;

    private final Clock clock;

    @Override
    public PagedResult<ChapterPreview> findByNovelId(NovelId novelId, int page, int size) {
        return chapterRepository.findByNovelId(novelId, page, size);
    }

    @Override
    public Chapter findById(ChapterId id) {
        return findChapterById(id);
    }

    @Override
    public Chapter create(NovelId novelId, String title, String content) {
        var novel = findNovelById(novelId);
        ensureIsAuthorFor(currentUserProvider.getCurrentUser(), novel);
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
        var novel = findNovelById(chapter.getNovelId());
        ensureIsAuthorFor(currentUserProvider.getCurrentUser(), novel);
        chapter.setTitle(title);
        chapter.setContent(content);
        return chapterRepository.save(chapter);
    }

    @Override
    public void delete(ChapterId id) {
        var chapter = findChapterById(id);
        var novel = findNovelById(chapter.getNovelId());
        ensureIsAuthorFor(currentUserProvider.getCurrentUser(), novel);
        chapterRepository.deleteById(id);
    }

    @Override
    public void deleteByNovelId(NovelId novelId) {
        var novel = findNovelById(novelId);
        ensureIsAuthorFor(currentUserProvider.getCurrentUser(), novel);
        var chapters = chapterRepository.findAllByNovelId(novelId);
        var chapterIds = chapters.stream().map(ChapterPreview::id).toList();
        chapterRepository.deleteAllById(chapterIds);
    }

    private Chapter findChapterById(ChapterId id) {
        return chapterRepository.findById(id)
            .orElseThrow(() -> new ChapterNotFoundException(id));
    }

    private Novel findNovelById(NovelId id) {
        return novelRepository.findById(id)
            .orElseThrow(() -> new NovelNotFoundException(id));
    }

    private static void ensureIsAuthorFor(CurrentUser user, Novel novel) {
        var isAuthor = novel.getAuthorId().value().equals(user.userId());
        if (!isAuthor) {
            throw new AccessDeniedException("Only author can modify the novel");
        }
    }
}
