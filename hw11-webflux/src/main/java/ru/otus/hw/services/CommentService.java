package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.domain.Comment;

public interface CommentService {

    Mono<Comment> findById(String id);

    Flux<Comment> findAllFor(String bookId);

    Mono<Comment> insert(String text, String bookId);

    Mono<Comment> update(String id, String text);

    Mono<Void> deleteById(String id);
}
