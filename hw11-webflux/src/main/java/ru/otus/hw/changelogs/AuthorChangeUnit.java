package ru.otus.hw.changelogs;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import ru.otus.hw.domain.Author;
import ru.otus.hw.repositories.AuthorRepository;

@RequiredArgsConstructor
@ChangeUnit(id = "authorChangeUnitId", order = "001", runAlways = true)
public class AuthorChangeUnit {

    private final AuthorRepository authorRepository;

    @Execution
    public void initAuthors() {
        Flux.range(1, 3)
            .map(i -> new Author(String.valueOf(i), "Author_" + i))
            .flatMap(authorRepository::save)
            .blockLast();
    }

    @RollbackExecution
    public void rollback() {
        Flux.from(authorRepository.deleteAll()).blockLast();
    }
}
