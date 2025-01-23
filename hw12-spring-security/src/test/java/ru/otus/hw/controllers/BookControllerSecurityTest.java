package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.otus.hw.config.SecurityConfiguration;
import ru.otus.hw.mappers.AuthorMapperImpl;
import ru.otus.hw.mappers.BookMapperImpl;
import ru.otus.hw.mappers.GenreMapperImpl;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;

@WebMvcTest(BookController.class)
@Import({SecurityConfiguration.class, BookMapperImpl.class, AuthorMapperImpl.class, GenreMapperImpl.class})
@TestPropertySource(properties = "mongock.enabled=false")
public class BookControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    static private List<Arguments> getEndpoints() {
        return List.of(
            Arguments.of(get("/books"), true, true),
            Arguments.of(get("/books"), false, true),
            Arguments.of(get("/books/edit/b1"), true, true),
            Arguments.of(get("/books/edit/b1"), false, false),
            Arguments.of(get("/books/create"), true, true),
            Arguments.of(get("/books/create"), false, false),
            Arguments.of(post("/books/create"), true, true),
            Arguments.of(post("/books/create"), false, false),
            Arguments.of(post("/books/edit/b1"), true, true),
            Arguments.of(post("/books/edit/b1"), false, false),
            Arguments.of(post("/books/delete/b1"), true, true),
            Arguments.of(post("/books/delete/b1"), false, false)
        );
    }

    @ParameterizedTest
    @MethodSource("getEndpoints")
    @DisplayName("Should test user access to the endpoint")
    void testEndpointAccess(
        MockHttpServletRequestBuilder request,
        boolean isAuthenticated,
        boolean shouldBeAllowed
    ) throws Exception {

        request = isAuthenticated
            ? request.with(user("admin"))
            : request.with(anonymous());

        var perform = mvc.perform(request.with(csrf()));

        if (shouldBeAllowed) {
            perform.andExpect(result -> assertThat(isNotAllowed(result)).isFalse());
        } else {
            perform.andExpect(result -> assertThat(isNotAllowed(result)).isTrue());
        }
    }

    private static boolean isNotAllowed(MvcResult result) {
        return isRedirectedToLoginPage(result) ||
               result.getResponse().getStatus() == 401 ||
               result.getResponse().getStatus() == 403;
    }

    private static boolean isRedirectedToLoginPage(MvcResult result) {
        var response = result.getResponse();
        var redirectedUrl = response.getRedirectedUrl();

        if (redirectedUrl == null) {
            return false;
        }

        return redirectedUrl.contains("/login");
    }
}
