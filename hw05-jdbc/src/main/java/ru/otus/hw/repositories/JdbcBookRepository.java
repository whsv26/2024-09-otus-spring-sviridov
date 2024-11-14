package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.util.Pair.toMap;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private static final String BOOK_ID = "book_id";

    private static final String BOOK_TITLE = "book_title";

    private static final String AUTHOR_ID = "author_id";

    private static final String AUTHOR_FULL_NAME = "author_full_name";

    private static final String GENRE_ID = "genre_id";

    private static final String GENRE_NAME = "genre_name";

    private final GenreRepository genreRepository;

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Optional<Book> findById(long id) {
        var sql = """
            select
                b.id as book_id,
                b.title as book_title,
                a.id as author_id,
                a.full_name as author_full_name,
                g.id as genre_id,
                g.name as genre_name
            from books b
            join authors a on b.author_id = a.id
            left join books_genres bg on b.id = bg.book_id
            left join genres g on bg.genre_id = g.id
            where b.id = :id
            """;
        var params = Map.of("id", id);

        return Optional.ofNullable(jdbc.query(sql, params, new BookResultSetExtractor()))
            .flatMap(books -> books.stream().findFirst());
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        var sql = "delete from books where id = :id";
        var params = Map.of("id", id);
        jdbc.update(sql, params);
    }

    private List<Book> getAllBooksWithoutGenres() {
        var sql = """
            select
                b.id as book_id,
                b.title as book_title,
                a.id as author_id,
                a.full_name as author_full_name
            from books b
            join authors a on b.author_id = a.id
            """;
        return jdbc.query(sql, new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        var sql = "select book_id, genre_id from books_genres";
        return jdbc.query(sql, new BookGenreRelationRowMapper());
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {

        var genresByGenreId = genres.stream()
            .map(genre -> Pair.of(genre.getId(), genre))
            .collect(toMap());

        var genresByBookId = relations.stream().collect(
            Collectors.groupingBy(
                relation -> relation.bookId,
                Collectors.mapping(
                    relation -> genresByGenreId.get(relation.genreId),
                    Collectors.toList()
                )
            )
        );

        for (var book : booksWithoutGenres) {
            var bookGenres = genresByBookId.getOrDefault(book.getId(), Collections.emptyList());
            book.setGenres(bookGenres);
        }
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        var sql = "insert into books (title, author_id) values (:book_title, :author_id)";
        var params = new MapSqlParameterSource(
            Map.of(AUTHOR_ID, book.getAuthor().getId(), BOOK_TITLE, book.getTitle())
        );

        jdbc.update(sql, params, keyHolder);
        Optional.ofNullable(keyHolder.getKeyAs(Long.class)).ifPresent(book::setId);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private Book update(Book book) {
        var sql = "update books set title = :book_title, author_id = :author_id where id = :book_id";
        var params = Map.of(
            BOOK_ID, book.getId(),
            BOOK_TITLE, book.getTitle(),
            AUTHOR_ID, book.getAuthor().getId()
        );

        var updatedRows = jdbc.update(sql, params);

        if (updatedRows == 0) {
            var message = "Book with id %d is not found for update".formatted(book.getId());
            throw new EntityNotFoundException(message);
        }

        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        var sql = "insert into books_genres(book_id, genre_id) values (:book_id, :genre_id)";
        var params = book.getGenres().stream().map(genre ->
            new MapSqlParameterSource(
                Map.of(BOOK_ID, book.getId(), GENRE_ID, genre.getId())
            )
        );

        jdbc.batchUpdate(sql, params.toArray(MapSqlParameterSource[]::new));
    }

    private void removeGenresRelationsFor(Book book) {
        var sql = "delete from books_genres where book_id = :book_id";
        var params = Map.of(BOOK_ID, book.getId());
        jdbc.update(sql, params);
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            var bookId = rs.getLong(BOOK_ID);
            var bookTitle = rs.getString(BOOK_TITLE);
            var authorId = rs.getLong(AUTHOR_ID);
            var authorFullName = rs.getString(AUTHOR_FULL_NAME);
            var author = new Author(authorId, authorFullName);
            var genres = Collections.<Genre>emptyList();

            return new Book(bookId, bookTitle, author, genres);
        }
    }

    private static class BookGenreRelationRowMapper implements RowMapper<BookGenreRelation> {

        @Override
        public BookGenreRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
            var bookId = rs.getLong(BOOK_ID);
            var genreId = rs.getLong(GENRE_ID);
            return new BookGenreRelation(bookId, genreId);
        }
    }

    private static class BookResultSetExtractor implements ResultSetExtractor<List<Book>> {

        @Override
        public List<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
            var booksById = new HashMap<Long, Book>();

            while (rs.next()) {
                var bookId = rs.getLong(BOOK_ID);
                var book = booksById.get(bookId);

                if (book != null) {
                    extractGenre(rs, book);
                } else {
                    book = new Book();
                    book.setGenres(new ArrayList<>());
                    book.setId(bookId);
                    extractTitle(rs, book);
                    extractAuthor(rs, book);
                    extractGenre(rs, book);
                    booksById.put(bookId, book);
                }
            }

            return booksById.values().stream().toList();
        }

        private static void extractTitle(ResultSet rs, Book book) throws SQLException {
            var bookTitle = rs.getString(BOOK_TITLE);
            book.setTitle(bookTitle);
        }

        private static void extractAuthor(ResultSet rs, Book book) throws SQLException {
            var authorId = rs.getLong(AUTHOR_ID);
            var authorFullName = rs.getString(AUTHOR_FULL_NAME);
            book.setAuthor(new Author(authorId, authorFullName));
        }

        private static void extractGenre(ResultSet rs, Book book) throws SQLException {
            var genreId = rs.getLong(GENRE_ID);
            if (genreId != 0) {
                var genreName = rs.getString(GENRE_NAME);
                book.getGenres().add(new Genre(genreId, genreName));
            }
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}
