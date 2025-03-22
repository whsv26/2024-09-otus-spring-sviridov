package me.whsv26.novel.api.application.port.in;

import me.whsv26.novel.api.domain.valueobject.GenreId;
import me.whsv26.novel.api.domain.entity.Novel;
import me.whsv26.novel.api.domain.valueobject.NovelId;

import java.util.List;

@FunctionalInterface
public interface UpdateNovelUseCase {

    Novel update(
        NovelId id,
        String title,
        String synopsis,
        List<GenreId> genres,
        List<String> tags
    );
}
