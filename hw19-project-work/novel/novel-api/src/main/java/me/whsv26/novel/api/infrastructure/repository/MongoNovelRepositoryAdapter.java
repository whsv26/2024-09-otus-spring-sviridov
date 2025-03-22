package me.whsv26.novel.api.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import me.whsv26.novel.api.application.port.out.NovelRepository;
import me.whsv26.novel.api.domain.entity.Novel;
import me.whsv26.novel.api.domain.valueobject.AuthorId;
import me.whsv26.novel.api.domain.valueobject.NovelId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MongoNovelRepositoryAdapter implements NovelRepository {

    private final MongoNovelRepository underlying;

    @Override
    public List<Novel> findByAuthorId(AuthorId authorId) {
        return underlying.findByAuthorId(authorId);
    }

    @Override
    public Optional<Novel> findById(NovelId id) {
        return underlying.findById(id);
    }

    @Override
    public Novel save(Novel entity) {
        return underlying.save(entity);
    }

    @Override
    public void delete(Novel entity) {
        underlying.delete(entity);
    }
}
