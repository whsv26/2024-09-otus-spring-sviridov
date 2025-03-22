package me.whsv26.novel.api.application.port.in;

import me.whsv26.novel.api.domain.NovelId;

public interface DeleteNovelChaptersUseCase {
    void deleteByNovelId(NovelId novelId);
}
