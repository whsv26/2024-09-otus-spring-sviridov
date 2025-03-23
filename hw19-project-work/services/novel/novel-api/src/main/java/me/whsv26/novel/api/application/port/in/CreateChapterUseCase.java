package me.whsv26.novel.api.application.port.in;

import me.whsv26.novel.api.domain.entity.Chapter;
import me.whsv26.novel.api.domain.valueobject.NovelId;

@FunctionalInterface
public interface CreateChapterUseCase {

    Chapter create(
        NovelId novelId,
        String title,
        String content
    );
}
