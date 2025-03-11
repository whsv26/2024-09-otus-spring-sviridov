package ru.otus.hw.services;

import ru.otus.hw.dtos.BookDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {

    Optional<BookDto> findById(Long id);

    List<BookDto> findAll();

    BookDto insert(String title, Long authorId, Set<Long> genresIds);

    BookDto update(Long id, String title, Long authorId, Set<Long> genresIds);

    void deleteById(Long id);
}
