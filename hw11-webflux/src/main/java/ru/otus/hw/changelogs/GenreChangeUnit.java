package ru.otus.hw.changelogs;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import ru.otus.hw.domain.Genre;
import ru.otus.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@ChangeUnit(id = "genreChangeUnitId", order = "002", runAlways = true)
public class GenreChangeUnit {

    private final GenreRepository genreRepository;

    @Execution
    public void initGenres() {
        Flux.range(1, 6)
            .map(i -> new Genre(String.valueOf(i), "Genre_" + i))
            .flatMap(genreRepository::save)
            .blockLast();
    }

    @RollbackExecution
    public void rollback() {
        Flux.from(genreRepository.deleteAll()).blockLast();
    }
}
