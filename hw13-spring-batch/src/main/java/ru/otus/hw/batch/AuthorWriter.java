package ru.otus.hw.batch;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Author;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@StepScope
public class AuthorWriter extends AbstractJDBCWriter<Author> {

    private final KeyMappingRepository<String, Long> keyMappingRepository;

    @Autowired
    public AuthorWriter(DataSource dataSource, KeyMappingRepository<String, Long> keyMappingRepository) {
        super(dataSource);
        this.keyMappingRepository = keyMappingRepository;
    }

    @Override
    public String getSQL() {
        return "INSERT INTO authors(full_name) VALUES (?)";
    }

    @Override
    public void setParameters(PreparedStatement statement, Author item) throws SQLException {
        statement.setString(1, item.getFullName());
    }

    @Override
    public void setGeneratedKeys(Author item, ResultSet generated) throws SQLException {
        long targetKey = generated.getLong(1);
        keyMappingRepository.set(item.getId(), targetKey, Author.class);
    }
}
