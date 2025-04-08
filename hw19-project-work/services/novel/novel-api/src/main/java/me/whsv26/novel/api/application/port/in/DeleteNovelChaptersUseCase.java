package me.whsv26.novel.api.application.port.in;

import me.whsv26.novel.api.domain.valueobject.NovelId;

@FunctionalInterface
public interface DeleteNovelChaptersUseCase {

    void deleteByNovelId(NovelId novelId);
}
