package me.whsv26.novel.api.infrastructure.repository;

import me.whsv26.novel.api.domain.entity.Novel;
import me.whsv26.novel.api.domain.valueobject.AuthorId;
import me.whsv26.novel.api.domain.valueobject.NovelId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoNovelRepository extends MongoRepository<Novel, NovelId> {

    List<Novel> findByAuthorId(AuthorId authorId);
}
