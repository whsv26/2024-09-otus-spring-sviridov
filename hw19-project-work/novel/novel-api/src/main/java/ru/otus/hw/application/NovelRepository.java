package ru.otus.hw.application;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.domain.AuthorId;
import ru.otus.hw.domain.Novel;
import ru.otus.hw.domain.NovelId;

import java.util.List;

public interface NovelRepository extends MongoRepository<Novel, NovelId> {

    List<Novel> findByAuthorId(AuthorId authorId);
}
