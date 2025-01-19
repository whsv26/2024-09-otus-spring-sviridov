package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.domain.Book;

import java.util.Set;

public interface BookService {

    Mono<Book> findById(String id);

    Flux<Book> findAll();

    Mono<Book> insert(String title, String authorId, Set<String> genresIds);

    Mono<Book> update(String id, String title, String authorId, Set<String> genresIds);

    Mono<Void> deleteById(String id);
}
