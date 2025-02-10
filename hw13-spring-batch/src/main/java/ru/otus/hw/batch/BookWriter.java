package ru.otus.hw.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import javax.sql.DataSource;
import java.util.Map;

@Component
@StepScope
@RequiredArgsConstructor
public class BookWriter implements ItemWriter<Book> {

    private final KeyMappingRepository<String, Long> keyMappingRepository;

    private final DataSource dataSource;

    @Override
    public void write(Chunk<? extends Book> chunk) {
        var booksInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("books")
            .usingGeneratedKeyColumns("id");

        var bookGenresInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("books_genres");

        for (var book : chunk) {
            var bookId = saveBook(book, booksInsert);
            saveBookGenres(book, bookId, bookGenresInsert);
        }
    }

    private long saveBook(Book book, SimpleJdbcInsert booksInsert) {
        var sourceAuthorId = book.getAuthor().getId();
        var targetAuthorId = keyMappingRepository.get(sourceAuthorId, Author.class);
        var params = Map.of("title", book.getTitle(), "author_id", targetAuthorId);
        var targetBookId = booksInsert.executeAndReturnKey(params).longValue();
        keyMappingRepository.set(book.getId(), targetBookId, Book.class);
        return targetBookId;
    }

    private void saveBookGenres(Book book, long targetBookId, SimpleJdbcInsert bookGenresInsert) {
        var batch = book.getGenres().stream()
            .map(genre -> {
                var targetGenreId = keyMappingRepository.get(genre.getId(), Genre.class);
                return new MapSqlParameterSource(
                    Map.of("book_id", targetBookId, "genre_id", targetGenreId)
                );
            })
            .toArray(SqlParameterSource[]::new);

        bookGenresInsert.executeBatch(batch);
    }
}
