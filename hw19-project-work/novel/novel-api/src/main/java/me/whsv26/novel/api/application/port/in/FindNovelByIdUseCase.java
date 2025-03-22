package me.whsv26.novel.api.application.port.in;

import me.whsv26.novel.api.domain.Novel;
import me.whsv26.novel.api.domain.NovelId;

public interface FindNovelByIdUseCase {
    Novel findById(NovelId id);
}
