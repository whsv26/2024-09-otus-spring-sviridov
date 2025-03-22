package me.whsv26.novel.api.application.port.in;

import me.whsv26.novel.api.domain.Chapter;
import me.whsv26.novel.api.domain.ChapterId;

public interface FindChapterByIdUseCase {
    Chapter findById(ChapterId id);
}
