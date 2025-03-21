package me.whsv26.novel.api.application;

import lombok.AllArgsConstructor;
import me.whsv26.novel.api.domain.AuthorId;
import me.whsv26.novel.api.domain.GenreId;
import me.whsv26.novel.api.domain.Novel;
import me.whsv26.novel.api.domain.NovelId;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;

@Service
@AllArgsConstructor
public class NovelServiceImpl implements NovelService {

    private final NovelRepository novelRepository;

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
        var novel = Novel.create(NovelId.next(), title, synopsis, authorId, genres, tags, clock);
        return novelRepository.save(novel);
    }

    @Transactional
    @Override
    public Novel update(NovelId id, String title, String synopsis, List<GenreId> genres, List<String> tags) {
        var novel = findNovelById(id);
        ensureIsAuthorFor(currentUserProvider.getCurrentUser(), novel);
        novel.update(title, synopsis, genres, tags, clock);
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
