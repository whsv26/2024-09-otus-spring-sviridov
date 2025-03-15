package me.whsv26.novel.api.application;

import me.whsv26.novel.api.domain.AuthorId;
import me.whsv26.novel.api.domain.GenreId;
import me.whsv26.novel.api.domain.Novel;
import me.whsv26.novel.api.domain.NovelId;

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
