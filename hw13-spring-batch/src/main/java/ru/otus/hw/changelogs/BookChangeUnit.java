package ru.otus.hw.changelogs;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Comparator;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@ChangeUnit(id = "bookChangeUnitId", order = "003", runAlways = true)
public class BookChangeUnit {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @Execution
    public void initBooks() {
        var authors = authorRepository.findAll();
        authors.sort(Comparator.comparing(Author::getId));
        var genres = genreRepository.findAll();
        genres.sort(Comparator.comparing(Genre::getId));

        IntStream.range(1, 4)
            .mapToObj(i -> new Book(
                String.valueOf(i),
                "BookTitle_" + i,
                authors.get(i - 1),
                genres.subList((i - 1) * 2, (i - 1) * 2 + 2)
            ))
            .forEach(bookRepository::save);
    }

    @RollbackExecution
    public void rollback() {
        bookRepository.deleteAll();
    }
}
