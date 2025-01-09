package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.domain.Author;

public interface AuthorRepository extends MongoRepository<Author, String> {
}
