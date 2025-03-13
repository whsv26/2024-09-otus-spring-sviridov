package ru.otus.hw.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.domain.AuthorId;
import ru.otus.hw.domain.GenreId;
import ru.otus.hw.domain.Novel;
import ru.otus.hw.domain.NovelId;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class NovelServiceImpl implements NovelService {

    private final NovelRepository novelRepository;

    private final OutboxEventRepository outboxEventRepository;

    private final Clock clock;

    private final ObjectMapper objectMapper;

    @Override
    public List<Novel> findByAuthorId(AuthorId authorId) {
        return novelRepository.findByAuthorId(authorId);
    }

    @Override
    public Novel findById(NovelId id) {
        return findNovelById(id);
    }

    @Override
    @Transactional
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
        var savedNovel = novelRepository.save(novel);

        var novelEvent = new NovelEvent(
            UUID.randomUUID().toString(),
            savedNovel.getId().value(),
            savedNovel.getTitle(),
            savedNovel.getSynopsis(),
            savedNovel.getAuthorId().value(),
            savedNovel.getGenres().stream().map(GenreId::value).toList(),
            savedNovel.getTags(),
            clock.instant().atZone(clock.getZone()).toLocalDateTime()
        );

        String payload;
        try {
            payload = objectMapper.writeValueAsString(novelEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        var outboxEvent = new OutboxEvent(
            novelEvent.id(),
            savedNovel.getClass().getName(),
            novelEvent.novelId(),
            novelEvent.getClass().getName(),
            novelEvent.createdAt(),
            payload,
            false
        );

        outboxEventRepository.save(outboxEvent);

        return savedNovel;
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
