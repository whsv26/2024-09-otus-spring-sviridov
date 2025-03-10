package ru.otus.hw.services;

import ru.otus.hw.dtos.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<CommentDto> findById(Long id);

    List<CommentDto> findAllFor(Long bookId);

    CommentDto insert(String text, Long bookId);

    CommentDto update(Long id, String text);

    void deleteById(Long id);
}
