package me.whsv26.novel.api.application.port.in;

import me.whsv26.novel.api.domain.GenreId;
import me.whsv26.novel.api.domain.Novel;
import me.whsv26.novel.api.domain.NovelId;

import java.util.List;

public interface UpdateNovelUseCase {
    Novel update(
        NovelId id,
        String title,
        String synopsis,
        List<GenreId> genres,
        List<String> tags
    );
}
