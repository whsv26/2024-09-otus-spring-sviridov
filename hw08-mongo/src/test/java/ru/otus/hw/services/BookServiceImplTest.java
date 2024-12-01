package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.exceptions.EntityNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("Сервис для работы с книгами ")
@SpringBootTest
class BookServiceImplTest {

    @Autowired
    private BookServiceImpl bookService;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("должен находить книгу по ее id")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void shouldReturnBookById(Book expectedBook) {
        var actualBook = bookService.findById(expectedBook.getId());

        assertThat(actualBook)
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(expectedBook);
    }

    @DisplayName("должен находить все книги")
    @Test
    void shouldReturnAllBooks() {
        var expectedBooks = dbBooks;
        var actualBooks =  bookService.findAll();

        assertThat(actualBooks)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrderElementsOf(expectedBooks);
    }

    @DisplayName("должен создавать новую книгу")
    @DirtiesContext
    @Test
    void shouldSaveNewBook() {
        var title = "BookTitle_10500";
        var author = dbAuthors.get(0);
        var genres = List.of(dbGenres.get(0), dbGenres.get(2));
        var genreIds = genres.stream().map(Genre::getId).collect(Collectors.toSet());
        var expectedBook = new Book(null, title, author, genres);
        var returnedBook = bookService.insert(title, author.getId(), genreIds);

        assertThat(returnedBook).isNotNull()
            .matches(book -> Objects.nonNull(book.getId()))
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(expectedBook);

        assertThat(bookService.findById(returnedBook.getId()))
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(returnedBook);
    }

    @DisplayName("должен возвращать ошибку при попытке создать книгу с несуществующим автором")
    @Test
    void shouldFailToSaveNewBookWithNonExistingAuthor() {
        var authorId = "999";
        var title = "BookTitle_10500";
        var genres = List.of(dbGenres.get(0), dbGenres.get(2));
        var genreIds = genres.stream().map(Genre::getId).collect(Collectors.toSet());

        assertThatExceptionOfType(EntityNotFoundException.class)
            .describedAs("Author with id %s not found".formatted(authorId))
            .isThrownBy(() -> bookService.insert(title, authorId, genreIds));
    }

    @DisplayName("должен возвращать ошибку при попытке создать книгу без жанров")
    @Test
    void shouldFailToSaveNewBookWithoutAnyGenre() {
        var title = "BookTitle_10500";
        var author = dbAuthors.get(0);

        assertThatExceptionOfType(IllegalArgumentException.class)
            .describedAs("Genres ids must not be null")
            .isThrownBy(() -> bookService.insert(title, author.getId(), Collections.emptySet()));
    }

    @DisplayName("должен возвращать ошибку при попытке создать книгу с несуществующим жанром")
    @Test
    void shouldFailToSaveNewBookWithNonExistingGenre() {
        var title = "BookTitle_10500";
        var author = dbAuthors.get(0);
        var genreIds = Set.of("1", "999");

        assertThatExceptionOfType(EntityNotFoundException.class)
            .describedAs("One or all genres with ids %s not found".formatted(genreIds))
            .isThrownBy(() -> bookService.insert(title, author.getId(), genreIds));
    }

    @DisplayName("должен изменять существующую книгу")
    @DirtiesContext
    @Test
    void shouldSaveUpdatedBook() {
        var title = "BookTitle_10500";
        var author = dbAuthors.get(2);
        var genres = List.of(dbGenres.get(4), dbGenres.get(5));
        var genreIds = genres.stream().map(Genre::getId).collect(Collectors.toSet());
        var bookId = "1";
        var expectedBook = new Book(bookId, title, author, genres);

        assertThat(bookService.findById(expectedBook.getId()))
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isNotEqualTo(expectedBook);

        var returnedBook = bookService.update(bookId, title, author.getId(), genreIds);
        assertThat(returnedBook)
            .isNotNull()
            .matches(book -> Objects.nonNull(book.getId()))
            .usingRecursiveComparison()
            .isEqualTo(expectedBook);

        assertThat(bookService.findById(returnedBook.getId()))
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id")
    @DirtiesContext
    @Test
    void shouldDeleteBook() {
        var bookId = "1";
        var book = bookService.findById(bookId);
        assertThat(book).isNotNull();
        bookService.deleteById(bookId);
        assertThat(bookService.findById(bookId)).isEmpty();
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
            .map(id -> new Author(String.valueOf(id), "Author_" + id))
            .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
            .map(id -> new Genre(String.valueOf(id), "Genre_" + id))
            .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
            .map(id -> new Book(
                String.valueOf(id),
                "BookTitle_" + id,
                dbAuthors.get(id - 1),
                dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
            ))
            .toList();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }
}