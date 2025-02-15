package ru.otus.hw.changelogs;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.domain.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.stream.IntStream;

@RequiredArgsConstructor
@ChangeUnit(id = "authorChangeUnitId", order = "001", runAlways = true)
public class AuthorChangeUnit {

    private final AuthorRepository authorRepository;

    @Execution
    public void initAuthors() {
        IntStream.range(1, 4)
            .mapToObj(i -> new Author(String.valueOf(i), "Author_" + i))
            .forEach(authorRepository::save);
    }

    @RollbackExecution
    public void rollback() {
        authorRepository.deleteAll();
    }
}
