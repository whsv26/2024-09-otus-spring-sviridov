package me.whsv26.novel.api.application.port.in;

import me.whsv26.novel.api.domain.AuthorId;
import me.whsv26.novel.api.domain.GenreId;
import me.whsv26.novel.api.domain.Novel;

import java.util.List;

public interface CreateNovelUseCase {
    Novel create(
        AuthorId authorId,
        String title,
        String synopsis,
        List<GenreId> genres,
        List<String> tags
    );
}
