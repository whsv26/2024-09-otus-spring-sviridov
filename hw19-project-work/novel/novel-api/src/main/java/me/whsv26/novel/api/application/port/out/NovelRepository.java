package me.whsv26.novel.api.application.port.out;

import me.whsv26.novel.api.domain.AuthorId;
import me.whsv26.novel.api.domain.Novel;
import me.whsv26.novel.api.domain.NovelId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NovelRepository extends MongoRepository<Novel, NovelId> {

    List<Novel> findByAuthorId(AuthorId authorId);
}
