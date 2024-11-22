package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("Сервис для работы с книгами ")
@SpringBootTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class BookServiceImplTest {

    @Autowired
    private BookServiceImpl bookService;

    private List<AuthorDto> dbAuthors;

    private List<GenreDto> dbGenres;

    private List<BookDto> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("должен находить книгу по ее id")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void shouldReturnBookById(BookDto expectedBook) {
        var actualBook = bookService.findById(expectedBook.id());

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
        var genreIds = genres.stream().map(GenreDto::id).collect(Collectors.toSet());
        var expectedBook = new BookDto(0, title, author, genres);
        var returnedBook = bookService.insert(title, author.id(), genreIds);

        assertThat(returnedBook).isNotNull()
            .matches(book -> book.id() > 0)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(expectedBook);

        assertThat(bookService.findById(returnedBook.id()))
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(returnedBook);
    }

    @DisplayName("должен возвращать ошибку при попытке создать книгу с несуществующим автором")
    @Test
    void shouldFailToSaveNewBookWithNonExistingAuthor() {
        var authorId = 999;
        var title = "BookTitle_10500";
        var genres = List.of(dbGenres.get(0), dbGenres.get(2));
        var genreIds = genres.stream().map(GenreDto::id).collect(Collectors.toSet());

        assertThatExceptionOfType(EntityNotFoundException.class)
            .describedAs("Author with id %d not found".formatted(authorId))
            .isThrownBy(() -> bookService.insert(title, authorId, genreIds));
    }

    @DisplayName("должен возвращать ошибку при попытке создать книгу без жанров")
    @Test
    void shouldFailToSaveNewBookWithoutAnyGenre() {
        var title = "BookTitle_10500";
        var author = dbAuthors.get(0);

        assertThatExceptionOfType(IllegalArgumentException.class)
            .describedAs("Genres ids must not be null")
            .isThrownBy(() -> bookService.insert(title, author.id(), Collections.emptySet()));
    }

    @DisplayName("должен возвращать ошибку при попытке создать книгу с несуществующим жанром")
    @Test
    void shouldFailToSaveNewBookWithNonExistingGenre() {
        var title = "BookTitle_10500";
        var author = dbAuthors.get(0);
        var genreIds = Set.of(1L, 999L);

        assertThatExceptionOfType(EntityNotFoundException.class)
            .describedAs("One or all genres with ids %s not found".formatted(genreIds))
            .isThrownBy(() -> bookService.insert(title, author.id(), genreIds));
    }

    @DisplayName("должен изменять существующую книгу")
    @DirtiesContext
    @Test
    void shouldSaveUpdatedBook() {
        var title = "BookTitle_10500";
        var author = dbAuthors.get(2);
        var genres = List.of(dbGenres.get(4), dbGenres.get(5));
        var genreIds = genres.stream().map(GenreDto::id).collect(Collectors.toSet());
        var bookId = 1;
        var expectedBook = new BookDto(bookId, title, author, genres);

        assertThat(bookService.findById(expectedBook.id()))
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isNotEqualTo(expectedBook);

        var returnedBook = bookService.update(bookId, title, author.id(), genreIds);
        assertThat(returnedBook)
            .isNotNull()
            .matches(book -> book.id() > 0)
            .usingRecursiveComparison()
            .isEqualTo(expectedBook);

        assertThat(bookService.findById(returnedBook.id()))
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id")
    @DirtiesContext
    @Test
    void shouldDeleteBook() {
        var bookId = 1L;
        var book = bookService.findById(bookId);
        assertThat(book).isNotNull();
        bookService.deleteById(bookId);
        assertThat(bookService.findById(bookId)).isEmpty();
    }

    private static List<AuthorDto> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
            .map(id -> new AuthorDto(id, "Author_" + id))
            .toList();
    }

    private static List<GenreDto> getDbGenres() {
        return IntStream.range(1, 7).boxed()
            .map(id -> new GenreDto(id, "Genre_" + id))
            .toList();
    }

    private static List<BookDto> getDbBooks(List<AuthorDto> dbAuthors, List<GenreDto> dbGenres) {
        return IntStream.range(1, 4).boxed()
            .map(id -> new BookDto(id,
                "BookTitle_" + id,
                dbAuthors.get(id - 1),
                dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
            ))
            .toList();
    }

    private static List<BookDto> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }
}