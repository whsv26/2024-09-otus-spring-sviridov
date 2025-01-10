package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw.dtos.AuthorDto;
import ru.otus.hw.dtos.BookDto;
import ru.otus.hw.dtos.BookUpsertDto;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.domain.Author;
import ru.otus.hw.domain.Book;
import ru.otus.hw.domain.Genre;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "mongock.enabled=false")
public class BookControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private BookService bookService;

    @Test
    @DisplayName("Should list all books")
    void shouldListAllBooks() {
        var author = new Author("a1", "Author");
        var genre = new Genre("g1", "Genre");
        var books = List.of(new Book("b1", "Book", author, List.of(genre)));

        when(bookService.findAll()).thenReturn(Flux.fromIterable(books));

        var expectedAuthor = new AuthorDto("a1", "Author");
        var expectedGenre = new GenreDto("g1", "Genre");
        var expectedBook = new BookDto("b1", "Book", expectedAuthor, List.of(expectedGenre));
        var expectedBooks = List.of(expectedBook);

        var actualBooks = client
            .get()
            .uri("/api/v1/books")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .returnResult(BookDto.class)
            .getResponseBody();

        StepVerifier.create(actualBooks)
            .expectNextSequence(expectedBooks)
            .verifyComplete();
    }

    @Test
    @DisplayName("Should view book")
    void shouldViewBook() {
        var author = new Author("a1", "Author");
        var genre = new Genre("g1", "Genre");
        var bookId = "b1";
        var book = new Book(bookId, "Book", author, List.of(genre));

        when(bookService.findById(bookId)).thenReturn(Mono.just(book));

        var expectedAuthor = new AuthorDto("a1", "Author");
        var expectedGenre = new GenreDto("g1", "Genre");
        var expectedBook = new BookDto(bookId, "Book", expectedAuthor, List.of(expectedGenre));

        var actualBooks = client
            .get()
            .uri("/api/v1/books/" + bookId)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .returnResult(BookDto.class)
            .getResponseBody();

        StepVerifier.create(actualBooks)
            .expectNext(expectedBook)
            .verifyComplete();
    }

    @Test
    @DisplayName("Should return error when book is not found")
    void shouldReturnErrorWhenBookIsNotFound() {
        var bookId = "b1";

        when(bookService.findById(bookId)).thenReturn(Mono.empty());

        var expectedError = new ErrorInfo(
            "BOOK_NOT_FOUND",
            "Book is not found",
            Map.of("id", bookId)
        );

        var actualError = client
            .get()
            .uri("/api/v1/books/" + bookId)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound()
            .returnResult(ErrorInfo.class)
            .getResponseBody();

        StepVerifier.create(actualError)
            .expectNext(expectedError)
            .verifyComplete();
    }

    @Test
    @DisplayName("Should create book")
    void shouldCreateBook() {
        var authorId = "a1";
        var author = new Author(authorId, "Author");
        var genreId = "g1";
        var genre = new Genre(genreId, "Genre");
        var title = "Book";
        var book = new Book("b1", title, author, List.of(genre));
        var requestBody = new BookUpsertDto(title, authorId, Set.of(genreId));

        when(bookService.insert(title, authorId, Set.of(genreId))).thenReturn(Mono.just(book));

        var expectedAuthor = new AuthorDto(authorId, "Author");
        var expectedGenre = new GenreDto(genreId, "Genre");
        var expectedBook = new BookDto("b1", title, expectedAuthor, List.of(expectedGenre));

        var actualBook = client
            .post()
            .uri("/api/v1/books")
            .contentType(APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange()
            .expectStatus().isOk()
            .returnResult(BookDto.class)
            .getResponseBody();

        StepVerifier.create(actualBook)
            .expectNext(expectedBook)
            .verifyComplete();

        verify(bookService, times(1))
            .insert(title, authorId, Set.of(genreId));
    }

    @Test
    @DisplayName("Should update book")
    void shouldUpdateBook() {
        var authorId = "a1";
        var author = new Author(authorId, "Author");
        var genreId = "g1";
        var genre = new Genre(genreId, "Genre");
        var title = "Book";
        var bookId = "b1";
        var book = new Book(bookId, title, author, List.of(genre));
        var requestBody = new BookUpsertDto(title, authorId, Set.of(genreId));

        when(bookService.update(bookId, title, authorId, Set.of(genreId))).thenReturn(Mono.just(book));

        var expectedAuthor = new AuthorDto(authorId, "Author");
        var expectedGenre = new GenreDto(genreId, "Genre");
        var expectedBook = new BookDto(bookId, title, expectedAuthor, List.of(expectedGenre));

        var actualBook = client
            .put()
            .uri("/api/v1/books/" + bookId)
            .contentType(APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange()
            .expectStatus().isOk()
            .returnResult(BookDto.class)
            .getResponseBody();

        StepVerifier.create(actualBook)
            .expectNext(expectedBook)
            .verifyComplete();

        verify(bookService, times(1))
            .update(bookId, title, authorId, Set.of(genreId));
    }

    @Test
    @DisplayName("Should delete book")
    void shouldDeleteBook() throws Exception {
        var bookId = "b1";

        client
            .delete()
            .uri("/api/v1/books/" + bookId)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .returnResult(Void.class);

        verify(bookService, times(1))
            .deleteById(bookId);
    }
}
