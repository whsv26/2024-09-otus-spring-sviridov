package ru.otus.hw.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.AuthorId;
import ru.otus.hw.domain.GenreId;
import ru.otus.hw.domain.Novel;
import ru.otus.hw.domain.NovelId;

import java.time.Clock;
import java.util.List;

@Service
@AllArgsConstructor
public class NovelServiceImpl implements NovelService {

    private final NovelRepository novelRepository;

    private final Clock clock;

    @Override
    public List<Novel> findByAuthorId(AuthorId authorId) {
        return novelRepository.findByAuthorId(authorId);
    }

    @Override
    public Novel findById(NovelId id) {
        return findNovelById(id);
    }

    @Override
    public Novel create(AuthorId authorId, String title, String synopsis, List<GenreId> genres, List<String> tags) {
        var novel = new Novel(
            NovelId.next(),
            title,
            synopsis,
            authorId,
            genres,
            tags,
            clock
        );
        return novelRepository.save(novel);
    }

    @Override
    public Novel update(NovelId id, String title, String synopsis, List<GenreId> genres, List<String> tags) {
        var novel = findNovelById(id);
        novel.setTitle(title);
        novel.setSynopsis(synopsis);
        novel.setGenres(genres);
        novel.setTags(tags);
        return novelRepository.save(novel);
    }

    @Override
    public void delete(NovelId id) {
        novelRepository.deleteById(id);
    }

    private Novel findNovelById(NovelId id) {
        return novelRepository.findById(id)
            .orElseThrow(() -> new NovelNotFoundException(id));
    }
}
