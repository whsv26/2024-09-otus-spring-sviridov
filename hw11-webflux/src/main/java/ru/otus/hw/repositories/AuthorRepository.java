package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.hw.domain.Author;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {
}
