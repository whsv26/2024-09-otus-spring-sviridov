package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import ru.otus.hw.config.SecurityConfiguration;
import ru.otus.hw.services.BookService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@WebMvcTest(BookController.class)
@Import(SecurityConfiguration.class)
public class BookControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    static private List<Arguments> getEndpoints() {
        return List.of(
            Arguments.of(get("/api/v1/books"), true, true, true),
            Arguments.of(get("/api/v1/books/1"), true, true, true),
            Arguments.of(post("/api/v1/books"), false, true, true),
            Arguments.of(put("/api/v1/books/1"), false, true, true),
            Arguments.of(delete("/api/v1/books/1"), false, false, true)
        );
    }

    @ParameterizedTest
    @MethodSource("getEndpoints")
    @DisplayName("Should test user access to the endpoint")
    void testEndpointAccess(
        MockHttpServletRequestBuilder request,
        boolean isAnonymousAllowed,
        boolean isEditorAllowed,
        boolean isAdminAllowed
    ) throws Exception {
        performAs(request, anonymous(), isAnonymousAllowed);
        performAs(request, user("editor").roles("EDITOR"), isEditorAllowed);
        performAs(request, user("admin").roles("ADMIN"), isAdminAllowed);
    }

    private void performAs(
        MockHttpServletRequestBuilder request,
        RequestPostProcessor user,
        boolean isAllowed
    ) throws Exception {
        var performResult = mvc.perform(request.with(user));
        if (isAllowed) {
            performResult.andExpect(result -> assertThat(isNotAllowed(result)).isFalse());
        } else {
            performResult.andExpect(result -> assertThat(isNotAllowed(result)).isTrue());
        }
    }

    private static boolean isNotAllowed(MvcResult result) {
        return result.getResponse().getStatus() == 401 ||
               result.getResponse().getStatus() == 403;
    }
}
