package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.SecurityConfiguration;
import ru.otus.hw.dtos.AuthorDto;
import ru.otus.hw.dtos.BookDto;
import ru.otus.hw.dtos.BookUpsertDto;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.mappers.BookMapperImpl;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import({SecurityConfiguration.class, BookMapperImpl.class})
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
        var expectedAuthor = new AuthorDto(1, "Author");
        var expectedGenre = new GenreDto(1, "Genre");
        var expectedBook = new BookDto(1, "Book", expectedAuthor, List.of(expectedGenre));
        var expectedBooks = List.of(expectedBook);

        when(bookService.findAll()).thenReturn(expectedBooks);

        mvc.perform(get("/api/v1/books").with(user("admin")))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(expectedBooks)));
    }

    @Test
    @DisplayName("Should view book")
    void shouldViewBook() throws Exception {
        var bookId = 1L;
        var expectedAuthor = new AuthorDto(1, "Author");
        var expectedGenre = new GenreDto(1, "Genre");
        var expectedBook = new BookDto(bookId, "Book", expectedAuthor, List.of(expectedGenre));

        when(bookService.findById(bookId)).thenReturn(Optional.of(expectedBook));

        mvc.perform(get("/api/v1/books/" + bookId).with(user("admin")))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(expectedBook), true));
    }

    @Test
    @DisplayName("Should return error when book is not found")
    void shouldReturnErrorWhenBookIsNotFound() throws Exception {
        var bookId = 1L;

        when(bookService.findById(bookId)).thenReturn(Optional.empty());

        var expectedError = new ErrorInfo(
            "BOOK_NOT_FOUND",
            "Book is not found",
            Map.of("id", bookId)
        );

        mvc.perform(get("/api/v1/books/" + bookId).with(user("admin")))
            .andExpect(status().isNotFound())
            .andExpect(content().json(mapper.writeValueAsString(expectedError), true));
    }

    @Test
    @DisplayName("Should create book")
    void shouldCreateBook() throws Exception {
        var authorId = 1L;
        var author = new AuthorDto(authorId, "Author");
        var genreId = 1L;
        var genre = new GenreDto(genreId, "Genre");
        var title = "Book";
        var book = new BookDto(1, title, author, List.of(genre));
        var requestBody = new BookUpsertDto(title, authorId, Set.of(genreId));
        var request = post("/api/v1/books")
            .with(user("admin").roles("ADMIN"))
            .contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(requestBody));

        when(bookService.insert(title, authorId, Set.of(genreId))).thenReturn(book);

        var expectedAuthor = new AuthorDto(authorId, "Author");
        var expectedGenre = new GenreDto(genreId, "Genre");
        var expectedBook = new BookDto(1, title, expectedAuthor, List.of(expectedGenre));

        mvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(expectedBook), true));

        verify(bookService, times(1))
            .insert(title, authorId, Set.of(genreId));
    }

    @Test
    @DisplayName("Should update book")
    void shouldUpdateBook() throws Exception {
        var authorId = 1L;
        var author = new AuthorDto(authorId, "Author");
        var genreId = 1L;
        var genre = new GenreDto(genreId, "Genre");
        var title = "Book";
        var bookId = 1L;
        var book = new BookDto(bookId, title, author, List.of(genre));
        var requestBody = new BookUpsertDto(title, authorId, Set.of(genreId));
        var request = put("/api/v1/books/" + bookId)
            .with(user("admin").roles("ADMIN"))
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
        var bookId = 1L;
        var request = delete("/api/v1/books/" + bookId)
            .with(user("admin").roles("ADMIN"));

        mvc.perform(request)
            .andExpect(status().isOk());

        verify(bookService, times(1))
            .deleteById(bookId);
    }
}
