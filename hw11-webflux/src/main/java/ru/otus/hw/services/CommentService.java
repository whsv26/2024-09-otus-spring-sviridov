package ru.otus.hw.services;

import ru.otus.hw.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<Comment> findById(String id);

    List<Comment> findAllFor(String bookId);

    Comment insert(String text, String bookId);

    Comment update(String id, String text);

    void deleteById(String id);
}
