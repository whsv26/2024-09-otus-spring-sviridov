package me.whsv26.novel.api.application.port.in;

import me.whsv26.novel.api.domain.Chapter;
import me.whsv26.novel.api.domain.NovelId;

public interface CreateChapterUseCase {
    Chapter create(
        NovelId novelId,
        String title,
        String content
    );
}
