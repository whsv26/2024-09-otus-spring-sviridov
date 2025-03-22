package me.whsv26.novel.api.application.port.in;

import me.whsv26.novel.api.domain.NovelId;

public interface DeleteNovelUseCase {
    void delete(NovelId id);
}
