package me.whsv26.novel.api.application.port.out;

import me.whsv26.novel.api.domain.entity.Novel;
import me.whsv26.novel.api.domain.valueobject.AuthorId;
import me.whsv26.novel.api.domain.valueobject.NovelId;

import java.util.List;
import java.util.Optional;

public interface NovelRepository {

    List<Novel> findByAuthorId(AuthorId authorId);

    Optional<Novel> findById(NovelId id);

    Novel save(Novel entity);

    void delete(Novel entity);
}
