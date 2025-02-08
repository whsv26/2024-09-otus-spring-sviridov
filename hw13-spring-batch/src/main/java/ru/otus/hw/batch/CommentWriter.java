package ru.otus.hw.batch;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@StepScope
public class CommentWriter extends AbstractJDBCWriter<Comment> {

    private final KeyMappingRepository<String, Long> keyMappingRepository;

    @Autowired
    public CommentWriter(DataSource dataSource, KeyMappingRepository<String, Long> keyMappingRepository) {
        super(dataSource);
        this.keyMappingRepository = keyMappingRepository;
    }

    @Override
    public String getSQL() {
        return "INSERT INTO comments(text, book_id) VALUES (?, ?)";
    }

    @Override
    public void setParameters(PreparedStatement statement, Comment item) throws SQLException {
        statement.setString(1, item.getText());
        var sourceBookId = item.getBook().getId();
        var targetBookId = keyMappingRepository.get(sourceBookId, Book.class);
        statement.setLong(2, targetBookId);
    }

    @Override
    public void setGeneratedKeys(Comment item, ResultSet generated) throws SQLException {
        long targetKey = generated.getLong(1);
        keyMappingRepository.set(item.getId(), targetKey, Comment.class);
    }
}
