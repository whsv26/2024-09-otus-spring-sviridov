package ru.otus.hw.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.KeyMappingRepository;

import javax.sql.DataSource;
import java.util.Map;

@Component
@StepScope
@RequiredArgsConstructor
public class AuthorWriter implements ItemWriter<Author> {

    private final KeyMappingRepository<String, Long> keyMappingRepository;

    private final DataSource dataSource;

    @Override
    public void write(Chunk<? extends Author> chunk) {
        var simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("authors")
            .usingGeneratedKeyColumns("id");

        for (var author : chunk) {
            var params = Map.of("full_name", author.getFullName());
            var targetKey = simpleJdbcInsert.executeAndReturnKey(params).longValue();
            keyMappingRepository.set(author.getId(), targetKey, Author.class);
        }
    }
}
