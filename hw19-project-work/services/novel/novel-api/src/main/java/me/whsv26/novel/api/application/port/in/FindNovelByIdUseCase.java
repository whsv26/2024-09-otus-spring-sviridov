package me.whsv26.novel.api.application.port.in;

import me.whsv26.novel.api.domain.entity.Novel;
import me.whsv26.novel.api.domain.valueobject.NovelId;

@FunctionalInterface
public interface FindNovelByIdUseCase {

    Novel findById(NovelId id);
}
