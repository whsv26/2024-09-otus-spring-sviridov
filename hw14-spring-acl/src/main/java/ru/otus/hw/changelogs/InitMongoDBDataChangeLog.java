package ru.otus.hw.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.domain.Author;
import ru.otus.hw.domain.Book;
import ru.otus.hw.domain.Comment;
import ru.otus.hw.domain.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    private final List<Author> authors = new ArrayList<>();

    private final List<Genre> genres = new ArrayList<>();

    private final List<Book> books = new ArrayList<>();

    @ChangeSet(order = "000", id = "dropDB", author = "whsv26", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "whsv26", runAlways = true)
    public void initAuthors(AuthorRepository repository) {
        IntStream.range(1, 4)
            .mapToObj(i -> new Author(String.valueOf(i), "Author_" + i))
            .forEach(author -> {
                repository.save(author);
                authors.add(author);
            });
    }

    @ChangeSet(order = "002", id = "initGenres", author = "whsv26", runAlways = true)
    public void initGenres(GenreRepository repository) {
        IntStream.range(1, 7)
            .mapToObj(i -> new Genre(String.valueOf(i), "Genre_" + i))
            .forEach(genre -> {
                repository.save(genre);
                genres.add(genre);
            });
    }

    @ChangeSet(order = "003", id = "initBooks", author = "whsv26", runAlways = true)
    public void initBooks(BookRepository repository) {
        IntStream.range(1, 4)
            .mapToObj(i -> new Book(
                String.valueOf(i),
                "BookTitle_" + i,
                authors.get(i - 1),
                genres.subList((i - 1) * 2, (i - 1) * 2 + 2)
            ))
            .forEach(book -> {
                repository.save(book);
                books.add(book);
            });
    }

    @ChangeSet(order = "004", id = "initComments", author = "whsv26", runAlways = true)
    public void initComments(CommentRepository repository) {
        IntStream.range(1, 3)
            .mapToObj(i -> new Comment(String.valueOf(i), books.get(0),"comment_" + i))
            .forEach(repository::save);
    }
}
