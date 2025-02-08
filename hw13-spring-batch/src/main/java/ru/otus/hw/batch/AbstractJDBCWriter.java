package ru.otus.hw.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@RequiredArgsConstructor
public abstract class AbstractJDBCWriter<T> implements ItemWriter<T> {

    private final DataSource dataSource;

    @Override
    public void write(Chunk<? extends T> chunk) throws Exception {
        var items = chunk.getItems();

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(getSQL(), Statement.RETURN_GENERATED_KEYS)) {

            for (var item : items) {
                setParameters(statement, item);
                statement.addBatch();
            }

            statement.executeBatch();

            try (var resultSet = statement.getGeneratedKeys()) {
                int index = 0;
                while (resultSet.next() && index < items.size()) {
                    var item = items.get(index);
                    setGeneratedKeys(item, resultSet);
                    index++;
                }
            }
        }
    }

    public abstract String getSQL();

    public abstract void setParameters(PreparedStatement statement, T item) throws SQLException;

    public abstract void setGeneratedKeys(T item, ResultSet generated) throws SQLException;
}
