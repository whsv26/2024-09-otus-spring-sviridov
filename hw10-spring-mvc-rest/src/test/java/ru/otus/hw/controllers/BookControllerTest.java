package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dtos.AuthorDto;
import ru.otus.hw.dtos.BookDto;
import ru.otus.hw.dtos.BookUpsertDto;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.mappers.BookMapperImpl;
import ru.otus.hw.domain.Author;
import ru.otus.hw.domain.Book;
import ru.otus.hw.domain.Genre;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(BookMapperImpl.class)
@TestPropertySource(properties = "mongock.enabled=false")
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService bookService;

    @Test
    @DisplayName("Should list all books")
    void shouldListAllBooks() throws Exception {
        var author = new Author("a1", "Author");
        var genre = new Genre("g1", "Genre");
        var books = List.of(new Book("b1", "Book", author, List.of(genre)));

        when(bookService.findAll()).thenReturn(books);

        var expectedAuthor = new AuthorDto("a1", "Author");
        var expectedGenre = new GenreDto("g1", "Genre");
        var expectedBook = new BookDto("b1", "Book", expectedAuthor, List.of(expectedGenre));
        var expectedBooks = List.of(expectedBook);

        mvc.perform(get("/api/v1/books"))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(expectedBooks)));
    }

    @Test
    @DisplayName("Should view book")
    void shouldViewBook() throws Exception {
        var author = new Author("a1", "Author");
        var genre = new Genre("g1", "Genre");
        var bookId = "b1";
        var book = new Book(bookId, "Book", author, List.of(genre));

        when(bookService.findById(bookId)).thenReturn(Optional.of(book));

        var expectedAuthor = new AuthorDto("a1", "Author");
        var expectedGenre = new GenreDto("g1", "Genre");
        var expectedBook = new BookDto(bookId, "Book", expectedAuthor, List.of(expectedGenre));

        mvc.perform(get("/api/v1/books/" + bookId))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(expectedBook), true));
    }

    @Test
    @DisplayName("Should return error when book is not found")
    void shouldReturnErrorWhenBookIsNotFound() throws Exception {
        var bookId = "b1";

        when(bookService.findById(bookId)).thenReturn(Optional.empty());

        var expectedError = new ErrorInfo(
            "BOOK_NOT_FOUND",
            "Book is not found",
            Map.of("id", bookId)
        );

        mvc.perform(get("/api/v1/books/" + bookId))
            .andExpect(status().isNotFound())
            .andExpect(content().json(mapper.writeValueAsString(expectedError), true));
    }

    @Test
    @DisplayName("Should create book")
    void shouldCreateBook() throws Exception {
        var authorId = "a1";
        var author = new Author(authorId, "Author");
        var genreId = "g1";
        var genre = new Genre(genreId, "Genre");
        var title = "Book";
        var book = new Book("b1", title, author, List.of(genre));
        var requestBody = new BookUpsertDto(title, authorId, Set.of(genreId));
        var request = post("/api/v1/books")
            .contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(requestBody));

        when(bookService.insert(title, authorId, Set.of(genreId))).thenReturn(book);

        var expectedAuthor = new AuthorDto(authorId, "Author");
        var expectedGenre = new GenreDto(genreId, "Genre");
        var expectedBook = new BookDto("b1", title, expectedAuthor, List.of(expectedGenre));

        mvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(expectedBook), true));

        verify(bookService, times(1))
            .insert(title, authorId, Set.of(genreId));
    }

    @Test
    @DisplayName("Should update book")
    void shouldUpdateBook() throws Exception {
        var authorId = "a1";
        var author = new Author(authorId, "Author");
        var genreId = "g1";
        var genre = new Genre(genreId, "Genre");
        var title = "Book";
        var bookId = "b1";
        var book = new Book(bookId, title, author, List.of(genre));
        var requestBody = new BookUpsertDto(title, authorId, Set.of(genreId));
        var request = put("/api/v1/books/" + bookId)
            .contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(requestBody));

        when(bookService.update(bookId, title, authorId, Set.of(genreId))).thenReturn(book);

        var expectedAuthor = new AuthorDto(authorId, "Author");
        var expectedGenre = new GenreDto(genreId, "Genre");
        var expectedBook = new BookDto(bookId, title, expectedAuthor, List.of(expectedGenre));

        mvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(expectedBook), true));

        verify(bookService, times(1))
            .update(bookId, title, authorId, Set.of(genreId));
    }

    @Test
    @DisplayName("Should delete book")
    void shouldDeleteBook() throws Exception {
        var bookId = "b1";
        var request = delete("/api/v1/books/" + bookId);

        mvc.perform(request)
            .andExpect(status().isOk());

        verify(bookService, times(1))
            .deleteById(bookId);
    }
}
