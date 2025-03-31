package me.whsv26.novel.api.application.service;

import lombok.AllArgsConstructor;
import me.whsv26.libs.auth.CurrentUser;
import me.whsv26.libs.auth.CurrentUserProvider;
import me.whsv26.novel.api.application.port.in.NovelUseCases;
import me.whsv26.novel.api.domain.valueobject.CreateNovelDetails;
import me.whsv26.novel.api.domain.valueobject.UpdateNovelDetails;
import me.whsv26.novel.api.infrastructure.repository.MongoNovelRepository;
import me.whsv26.novel.api.domain.valueobject.AuthorId;
import me.whsv26.novel.api.domain.valueobject.GenreId;
import me.whsv26.novel.api.domain.entity.Novel;
import me.whsv26.novel.api.domain.valueobject.NovelId;
import me.whsv26.novel.api.domain.exception.NovelNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;

@Service
@AllArgsConstructor
public class NovelService implements NovelUseCases {

    private final MongoNovelRepository novelRepository;

    private final CurrentUserProvider currentUserProvider;

    private final Clock clock;

    @Override
    public List<Novel> findByAuthorId(AuthorId authorId) {
        return novelRepository.findByAuthorId(authorId);
    }

    @Override
    public Novel findById(NovelId id) {
        return findNovelById(id);
    }

    @Transactional
    @Override
    public Novel create(AuthorId authorId, String title, String synopsis, List<GenreId> genres, List<String> tags) {
        var details = new CreateNovelDetails(NovelId.next(), title, synopsis, authorId, genres, tags);
        var novel = Novel.create(details, clock);
        return novelRepository.save(novel);
    }

    @Transactional
    @Override
    public Novel update(NovelId id, String title, String synopsis, List<GenreId> genres, List<String> tags) {
        var novel = findNovelById(id);
        ensureIsAuthorFor(currentUserProvider.getCurrentUser(), novel);
        var details = new UpdateNovelDetails(title, synopsis, genres, tags);
        novel.update(details, clock);
        return novelRepository.save(novel);
    }

    @Transactional
    @Override
    public void delete(NovelId id) {
        var novel = findNovelById(id);
        ensureIsAuthorFor(currentUserProvider.getCurrentUser(), novel);
        novel.delete(clock);
        novelRepository.delete(novel);
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
