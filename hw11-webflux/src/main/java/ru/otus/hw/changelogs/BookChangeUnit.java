package ru.otus.hw.changelogs;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.domain.Author;
import ru.otus.hw.domain.Book;
import ru.otus.hw.domain.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Comparator;
import java.util.function.Function;

@RequiredArgsConstructor
@ChangeUnit(id = "bookChangeUnitId", order = "003", runAlways = true)
public class BookChangeUnit {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @Execution
    public void initBooks() {
        Mono.zip(
            authorRepository.findAll().sort(Comparator.comparing(Author::getId)).collectList(),
            genreRepository.findAll().sort(Comparator.comparing(Genre::getId)).collectList(),
            (authors, genres) ->
                Flux.range(1, 3)
                    .map(i -> new Book(
                        String.valueOf(i),
                        "BookTitle_" + i,
                        authors.get(i - 1),
                        genres.subList((i - 1) * 2, (i - 1) * 2 + 2)
                    ))
                    .flatMap(bookRepository::save)
        ).flux().flatMap(Function.identity()).blockLast();
    }

    @RollbackExecution
    public void rollback() {
        Flux.from(bookRepository.deleteAll()).blockLast();
    }
}
