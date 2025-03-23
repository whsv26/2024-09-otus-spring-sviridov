package me.whsv26.novel.api.application.port.in;

import me.whsv26.novel.api.domain.valueobject.AuthorId;
import me.whsv26.novel.api.domain.valueobject.GenreId;
import me.whsv26.novel.api.domain.entity.Novel;

import java.util.List;

@FunctionalInterface
public interface CreateNovelUseCase {

    Novel create(
        AuthorId authorId,
        String title,
        String synopsis,
        List<GenreId> genres,
        List<String> tags
    );
}
