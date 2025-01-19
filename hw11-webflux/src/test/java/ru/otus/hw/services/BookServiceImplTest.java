package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.util.Pair;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;
import ru.otus.hw.domain.Author;
import ru.otus.hw.domain.Book;
import ru.otus.hw.domain.Genre;
import ru.otus.hw.exceptions.AuthorNotFoundException;
import ru.otus.hw.exceptions.GenresNotFoundException;
import ru.otus.hw.MongockTestConfig;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с книгами ")
@SpringBootTest
@Import(MongockTestConfig.class)
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
        StepVerifier
            .create(bookService.findById(expectedBook.getId()))
            .assertNext(book -> assertThat(book)
                .usingRecursiveComparison()
                .isEqualTo(expectedBook)
            )
            .verifyComplete();
    }

    @DisplayName("должен находить все книги")
    @Test
    void shouldReturnAllBooks() {
        var expectedBooks = dbBooks;
        var actualBooks = bookService.findAll().sort(Comparator.comparing(Book::getId));
        var verifier = StepVerifier.create(actualBooks);

        expectedBooks.forEach(expectedBook ->
            verifier.assertNext(actualBook ->
                assertThat(actualBook)
                    .usingRecursiveComparison()
                    .isEqualTo(expectedBook)
            )
        );

        verifier.verifyComplete();
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

        var insertMono = bookService.insert(title, author.getId(), genreIds);

        StepVerifier.create(insertMono)
            .assertNext(returnedBook ->
                assertThat(returnedBook).isNotNull()
                    .matches(book -> Objects.nonNull(book.getId()))
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(expectedBook)
            )
            .verifyComplete();

        var returnedWithActualMono = insertMono.flatMap(returnedBook ->
            bookService.findById(returnedBook.getId())
                .map(actualBook -> Pair.of(returnedBook, actualBook))
        );

        StepVerifier.create(returnedWithActualMono)
            .assertNext(returnedWithActual ->
                assertThat(returnedWithActual.getSecond())
                    .usingRecursiveComparison()
                    .isEqualTo(returnedWithActual.getFirst())
            )
            .verifyComplete();
    }

    @DisplayName("должен возвращать ошибку при попытке создать книгу с несуществующим автором")
    @Test
    void shouldFailToSaveNewBookWithNonExistingAuthor() {
        var authorId = "999";
        var title = "BookTitle_10500";
        var genres = List.of(dbGenres.get(0), dbGenres.get(2));
        var genreIds = genres.stream().map(Genre::getId).collect(Collectors.toSet());

        StepVerifier.create(bookService.insert(title, authorId, genreIds))
            .expectError(AuthorNotFoundException.class)
            .verify();
    }

    @DisplayName("должен возвращать ошибку при попытке создать книгу без жанров")
    @Test
    void shouldFailToSaveNewBookWithoutAnyGenre() {
        var title = "BookTitle_10500";
        var author = dbAuthors.get(0);

        StepVerifier.create(bookService.insert(title, author.getId(), Collections.emptySet()))
            .expectErrorMessage("Genres ids must not be null")
            .verify();
    }

    @DisplayName("должен возвращать ошибку при попытке создать книгу с несуществующим жанром")
    @Test
    void shouldFailToSaveNewBookWithNonExistingGenre() {
        var title = "BookTitle_10500";
        var author = dbAuthors.get(0);
        var genreIds = Set.of("1", "999");

        StepVerifier.create(bookService.insert(title, author.getId(), genreIds))
            .expectError(GenresNotFoundException.class)
            .verify();
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

        StepVerifier.create(bookService.findById(bookId))
            .assertNext(book -> assertThat(book)
                .usingRecursiveComparison()
                .isNotEqualTo(expectedBook)
            )
            .verifyComplete();

        Mono<Book> returnedBookMono = bookService.update(bookId, title, author.getId(), genreIds);
        StepVerifier.create(returnedBookMono)
            .assertNext(returnedBook -> assertThat(returnedBook)
                .isNotNull()
                .matches(book -> Objects.nonNull(book.getId()))
                .usingRecursiveComparison()
                .isEqualTo(expectedBook)
            )
            .verifyComplete();

        var returnedWIthActualBookMono = returnedBookMono.flatMap(returnedBook ->
            bookService.findById(returnedBook.getId())
                .map(actualBook -> Pair.of(returnedBook, actualBook))
        );

        StepVerifier.create(returnedWIthActualBookMono)
            .assertNext(returnedWIthActualBook ->
                assertThat(returnedWIthActualBook.getSecond())
                    .usingRecursiveComparison()
                    .isEqualTo(returnedWIthActualBook.getFirst())
            )
            .verifyComplete();
    }

    @DisplayName("должен удалять книгу по id")
    @DirtiesContext
    @Test
    void shouldDeleteBook() {
        var bookId = "1";

        StepVerifier.create(bookService.findById(bookId))
            .expectNextCount(1)
            .verifyComplete();

        StepVerifier.create(
            bookService.deleteById(bookId)
                .zipWith(bookService.findById(bookId))
                .map(Tuple2::getT2)
        ).verifyComplete();
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