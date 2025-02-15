package ru.otus.hw.changelogs;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.domain.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.stream.IntStream;

@RequiredArgsConstructor
@ChangeUnit(id = "genreChangeUnitId", order = "002", runAlways = true)
public class GenreChangeUnit {

    private final GenreRepository genreRepository;

    @Execution
    public void initGenres() {
        IntStream.range(1, 7)
            .mapToObj(i -> new Genre(String.valueOf(i), "Genre_" + i))
            .forEach(genreRepository::save);
    }

    @RollbackExecution
    public void rollback() {
        genreRepository.deleteAll();
    }
}
