package ru.otus.hw.services;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Book;
import ru.otus.hw.domain.Genre;
import ru.otus.hw.exceptions.AuthorNotFoundException;
import ru.otus.hw.exceptions.GenreNotFoundException;
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

    @Override
    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll()
            .stream()
            .toList();
    }

    @Override
    public Book insert(String title, String authorId, Set<String> genresIds) {
        return save(null, title, authorId, genresIds);
    }

    @Override
    public Book update(String id, String title, String authorId, Set<String> genresIds) {
        return save(id, title, authorId, genresIds);
    }

    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }

    private Book save(String id, String title, String authorId, Set<String> genresIds) {
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
