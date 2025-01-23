package ru.otus.hw.controllers;

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
import ru.otus.hw.dtos.BookFormDto;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.mappers.AuthorMapperImpl;
import ru.otus.hw.mappers.BookMapperImpl;
import ru.otus.hw.mappers.GenreMapperImpl;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
@Import({BookMapperImpl.class, AuthorMapperImpl.class, GenreMapperImpl.class})
@TestPropertySource(properties = "mongock.enabled=false")
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @Test
    @DisplayName("Should render list page with correct view and model attributes")
    void shouldRenderListPageWithCorrectViewAndModelAttributes() throws Exception {
        var author = new Author("a1", "Author");
        var genre = new Genre("g1", "Genre");
        var books = List.of(new Book("b1", "Book", author, List.of(genre)));

        when(bookService.findAll()).thenReturn(books);

        var expectedAuthor = new AuthorDto("a1", "Author");
        var expectedGenre = new GenreDto("g1", "Genre");
        var expectedBook = new BookDto("b1", "Book", expectedAuthor, List.of(expectedGenre));
        var expectedBooks = List.of(expectedBook);

        mvc.perform(get("/books").with(user("admin")))
            .andExpect(view().name("listBooks"))
            .andExpect(model().attribute("books", expectedBooks))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should render edit page with correct view and model attributes")
    void shouldRenderEditPageWithCorrectViewAndModelAttributes() throws Exception {
        var authorId = "a1";
        var authorName = "Author";
        var author = new Author(authorId, authorName);

        var genreId = "g1";
        var genreName = "Genre";
        var genre = new Genre(genreId, genreName);

        var bookId = "b1";
        var bookTitle = "Book";
        var book = new Book(bookId, bookTitle, author, List.of(genre));

        when(authorService.findAll()).thenReturn(List.of(author));
        when(genreService.findAll()).thenReturn(List.of(genre));
        when(bookService.findById(bookId)).thenReturn(Optional.of(book));

        var expectedAuthors = List.of(new AuthorDto(authorId, authorName));
        var expectedGenres = List.of(new GenreDto(genreId, genreName));
        var expectedBook = new BookFormDto(bookTitle, authorId, Set.of(genreId));

        mvc.perform(get("/books/edit/" + bookId).with(user("admin")))
            .andExpect(view().name("editBook"))
            .andExpect(model().attribute("bookId", bookId))
            .andExpect(model().attribute("book", expectedBook))
            .andExpect(model().attribute("authors", expectedAuthors))
            .andExpect(model().attribute("genres", expectedGenres))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should render error page when book not found")
    void shouldRenderErrorPageWhenBookNotFound() throws Exception {
        var bookId = "b1";

        when(bookService.findById(bookId)).thenThrow(new BookNotFoundException(bookId));

        mvc.perform(get("/books/edit/" + bookId).with(user("admin")))
            .andExpect(view().name("customError"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should create book and redirect to listing page")
    void shouldCreateBookAndRedirectToListingPage() throws Exception {
        var title = "Book";
        var author = "a1";
        var genres = Set.of("g1");
        var request = post("/books/create")
            .param("title", title)
            .param("authorId", author)
            .param("genreIds", genres.toArray(String[]::new));

        mvc.perform(request.with(user("admin")).with(csrf()))
            .andExpect(view().name("redirect:/books"))
            .andExpect(status().is3xxRedirection());

        verify(bookService, times(1))
            .insert(title, author, genres);
    }

    @Test
    @DisplayName("Should update book and redirect to listing page")
    void shouldUpdateBookAndRedirectToListingPage() throws Exception {
        var bookId = "b1";
        var updatedTitle = "Updated Book";
        var updatedAuthor = "a2";
        var updatedGenres = Set.of("g1", "g2");
        var request = post("/books/edit/" + bookId)
            .param("title", updatedTitle)
            .param("authorId", updatedAuthor)
            .param("genreIds", updatedGenres.toArray(String[]::new));

        mvc.perform(request.with(user("admin")).with(csrf()))
            .andExpect(view().name("redirect:/books"))
            .andExpect(status().is3xxRedirection());

        verify(bookService, times(1))
            .update(bookId, updatedTitle, updatedAuthor, updatedGenres);
    }

    @Test
    @DisplayName("Should delete book and redirect to listing page")
    void shouldDeleteBookAndRedirectToListingPage() throws Exception {
        var bookId = "b1";
        var request = post("/books/delete/" + bookId).with(csrf());

        mvc.perform(request.with(user("admin")))
            .andExpect(view().name("redirect:/books"))
            .andExpect(status().is3xxRedirection());

        verify(bookService, times(1))
            .deleteById(bookId);
    }
}
