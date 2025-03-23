package me.whsv26.novel.api.application.port.in;

import me.whsv26.novel.api.domain.valueobject.AuthorId;
import me.whsv26.novel.api.domain.entity.Novel;

import java.util.List;

@FunctionalInterface
public interface FindNovelByAuthorIdUseCase {

    List<Novel> findByAuthorId(AuthorId authorId);
}
