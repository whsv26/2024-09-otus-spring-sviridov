package ru.otus.hw.services;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.domain.Book;
import ru.otus.hw.domain.Genre;
import ru.otus.hw.exceptions.AuthorNotFoundException;
import ru.otus.hw.exceptions.GenresNotFoundException;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.HashSet;
import java.util.List;
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
    public Mono<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    public Flux<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Mono<Book> insert(String title, String authorId, Set<String> genresIds) {
        return save(null, title, authorId, genresIds);
    }

    @Override
    public Mono<Book> update(String id, String title, String authorId, Set<String> genresIds) {
        return save(id, title, authorId, genresIds);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return bookRepository.deleteById(id);
    }

    private Mono<Book> save(String id, String title, String authorId, Set<String> genresIds) {
        if (isEmpty(genresIds)) {
            return Mono.error(new IllegalArgumentException("Genres ids must not be null"));
        }

        var maybeAuthor = authorRepository.findById(authorId)
            .switchIfEmpty(Mono.error(new AuthorNotFoundException(authorId)));

        var maybeGenres = genreRepository.findAllById(genresIds).collectList()
            .flatMap(foundGenres -> ensureAllGenresExist(genresIds, foundGenres));

        return Mono.zip(maybeAuthor, maybeGenres, (author, genres) -> new Book(id, title, author, genres))
            .flatMap(bookRepository::save);
    }

    private static Mono<List<Genre>> ensureAllGenresExist(Set<String> genresIds, List<Genre> foundGenres) {
        var foundGenreIds = foundGenres.stream().map(Genre::getId).collect(Collectors.toSet());
        var notFoundGenreIds = Sets.difference(genresIds, foundGenreIds);

        return isEmpty(notFoundGenreIds)
            ? Mono.just(foundGenres)
            : Mono.error(new GenresNotFoundException(new HashSet<>(notFoundGenreIds)));
    }
}
