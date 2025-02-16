package ru.otus.hw.services;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.domain.Book;
import ru.otus.hw.domain.Genre;
import ru.otus.hw.dtos.BookDto;
import ru.otus.hw.exceptions.AuthorNotFoundException;
import ru.otus.hw.exceptions.GenreNotFoundException;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<BookDto> findById(Long id) {
        return bookRepository.findById(id).map(bookMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        return bookRepository.findAll()
            .stream()
            .map(bookMapper::toDto)
            .toList();
    }

    @Override
    @Transactional
    public BookDto insert(String title, Long authorId, Set<Long> genresIds) {
        var book = save(null, title, authorId, genresIds);
        return bookMapper.toDto(book);
    }

    @Override
    @Transactional
    public BookDto update(Long id, String title, Long authorId, Set<Long> genresIds) {
        var book = save(id, title, authorId, genresIds);
        return bookMapper.toDto(book);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    private Book save(Long id, String title, Long authorId, Set<Long> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        var author = authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotFoundException(authorId));
        var genres = genreRepository.findAllById(genresIds);

        var foundGenreIds = genres.stream().map(Genre::getId).collect(Collectors.toSet());
        var notFoundGenreIds = Sets.difference(genresIds, foundGenreIds);

        if (!isEmpty(notFoundGenreIds)) {
            var notFoundGenreId = notFoundGenreIds.stream().findAny().get();
            throw new GenreNotFoundException(notFoundGenreId);
        }

        var book = new Book(id, title, author, genres);
        return bookRepository.save(book);
    }
}
