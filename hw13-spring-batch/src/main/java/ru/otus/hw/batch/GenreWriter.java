package ru.otus.hw.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.KeyMappingRepository;

import javax.sql.DataSource;
import java.util.Map;

@Component
@StepScope
@RequiredArgsConstructor
public class GenreWriter implements ItemWriter<Genre> {

    private final KeyMappingRepository<String, Long> keyMappingRepository;

    private final DataSource dataSource;

    @Override
    public void write(Chunk<? extends Genre> chunk) {
        var simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("genres")
            .usingGeneratedKeyColumns("id");

        for (var genre : chunk) {
            var params = Map.of("name", genre.getName());
            var targetKey = simpleJdbcInsert.executeAndReturnKey(params).longValue();
            keyMappingRepository.set(genre.getId(), targetKey, Genre.class);
        }
    }
}
