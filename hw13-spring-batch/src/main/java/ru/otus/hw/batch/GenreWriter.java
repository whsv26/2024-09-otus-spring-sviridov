package ru.otus.hw.batch;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Genre;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@StepScope
public class GenreWriter extends AbstractJDBCWriter<Genre> {

    private final KeyMappingRepository<String, Long> keyMappingRepository;

    @Autowired
    public GenreWriter(DataSource dataSource, KeyMappingRepository<String, Long> keyMappingRepository) {
        super(dataSource);
        this.keyMappingRepository = keyMappingRepository;
    }

    @Override
    public String getSQL() {
        return "INSERT INTO genres(name) VALUES (?)";
    }

    @Override
    public void setParameters(PreparedStatement statement, Genre item) throws SQLException {
        statement.setString(1, item.getName());
    }

    @Override
    public void setGeneratedKeys(Genre item, ResultSet generated) throws SQLException {
        long targetKey = generated.getLong(1);
        keyMappingRepository.set(item.getId(), targetKey, Genre.class);
    }
}
