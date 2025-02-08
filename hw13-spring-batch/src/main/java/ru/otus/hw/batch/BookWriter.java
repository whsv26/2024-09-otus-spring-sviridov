package ru.otus.hw.batch;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@StepScope
public class BookWriter extends AbstractJDBCWriter<Book> {

    private final KeyMappingRepository<String, Long> keyMappingRepository;

    @Autowired
    public BookWriter(DataSource dataSource, KeyMappingRepository<String, Long> keyMappingRepository) {
        super(dataSource);
        this.keyMappingRepository = keyMappingRepository;
    }

    @Override
    public String getSQL() {
        return "INSERT INTO books(title, author_id) VALUES (?, ?)";
    }

    @Override
    public void setParameters(PreparedStatement statement, Book item) throws SQLException {
        statement.setString(1, item.getTitle());
        var sourceAuthorId = item.getAuthor().getId();
        var targetAuthorId = keyMappingRepository.get(sourceAuthorId, Author.class);
        statement.setLong(2, targetAuthorId);
    }

    @Override
    public void setGeneratedKeys(Book item, ResultSet generated) throws SQLException {
        long targetKey = generated.getLong(1);
        keyMappingRepository.set(item.getId(), targetKey, Book.class);
    }
}
