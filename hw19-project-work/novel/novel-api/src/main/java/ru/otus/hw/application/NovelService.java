package ru.otus.hw.application;

import ru.otus.hw.domain.AuthorId;
import ru.otus.hw.domain.GenreId;
import ru.otus.hw.domain.Novel;
import ru.otus.hw.domain.NovelId;

import java.util.List;

public interface NovelService {

    List<Novel> findByAuthorId(AuthorId authorId);

    Novel findById(NovelId id);

    Novel create(
        AuthorId authorId,
        String title,
        String synopsis,
        List<GenreId> genres,
        List<String> tags
    );

    Novel update(
        NovelId id,
        String title,
        String synopsis,
        List<GenreId> genres,
        List<String> tags
    );

    void delete(NovelId id);
}
