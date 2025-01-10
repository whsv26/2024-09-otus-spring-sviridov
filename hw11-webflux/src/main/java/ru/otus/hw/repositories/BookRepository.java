package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.hw.domain.Book;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {
}
