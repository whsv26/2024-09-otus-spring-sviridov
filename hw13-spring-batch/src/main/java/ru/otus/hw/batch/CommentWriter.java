package ru.otus.hw.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
@StepScope
@RequiredArgsConstructor
public class CommentWriter implements ItemWriter<Comment> {

    public static final String SQL = "INSERT INTO comments(text, book_id) VALUES (?, ?)";

    private final KeyMappingRepository<String, Long> keyMappingRepository;

    private final DataSource dataSource;

    @Override
    public void write(Chunk<? extends Comment> chunk) throws Exception {
        var comments = chunk.getItems();

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

            for (var comment : comments) {
                addBatchStatement(comment, statement);
            }

            statement.executeBatch();

            try (var resultSet = statement.getGeneratedKeys()) {
                int index = 0;
                while (resultSet.next() && index < comments.size()) {
                    var comment = comments.get(index);
                    saveKeyMapping(resultSet, comment);
                    index++;
                }
            }
        }
    }

    private void saveKeyMapping(ResultSet resultSet, Comment comment) throws SQLException {
        var targetKey = resultSet.getLong(1);
        var sourceKey = comment.getId();
        keyMappingRepository.set(sourceKey, targetKey, Comment.class);
    }

    private void addBatchStatement(Comment comment, PreparedStatement statement) throws SQLException {
        statement.setString(1, comment.getText());
        var sourceBookId = comment.getBook().getId();
        var targetBookId = keyMappingRepository.get(sourceBookId, Book.class);
        statement.setLong(2, targetBookId);
        statement.addBatch();
    }
}
