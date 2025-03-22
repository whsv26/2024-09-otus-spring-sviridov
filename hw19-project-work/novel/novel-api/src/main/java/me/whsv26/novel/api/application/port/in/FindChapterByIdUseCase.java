package me.whsv26.novel.api.application.port.in;

import me.whsv26.novel.api.domain.entity.Chapter;
import me.whsv26.novel.api.domain.valueobject.ChapterId;

@FunctionalInterface
public interface FindChapterByIdUseCase {

    Chapter findById(ChapterId id);
}
