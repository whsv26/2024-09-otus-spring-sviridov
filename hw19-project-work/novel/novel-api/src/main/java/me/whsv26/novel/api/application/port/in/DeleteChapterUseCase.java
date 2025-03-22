package me.whsv26.novel.api.application.port.in;

import me.whsv26.novel.api.domain.ChapterId;

public interface DeleteChapterUseCase {
    void delete(ChapterId id);
}
