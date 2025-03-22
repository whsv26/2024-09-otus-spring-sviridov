package me.whsv26.novel.api.application.port.in;

import me.whsv26.novel.api.domain.AuthorId;
import me.whsv26.novel.api.domain.Novel;

import java.util.List;

public interface FinNovelByAuthorIdUseCase {
    List<Novel> findByAuthorId(AuthorId authorId);
}
