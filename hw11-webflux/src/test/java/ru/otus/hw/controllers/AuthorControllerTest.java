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
import ru.otus.hw.domain.Author;
import ru.otus.hw.dtos.AuthorDto;
import ru.otus.hw.mappers.AuthorMapperImpl;
import ru.otus.hw.services.AuthorService;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
@Import(AuthorMapperImpl.class)
@TestPropertySource(properties = "mongock.enabled=false")
public class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AuthorService authorService;

    @Test
    @DisplayName("Should list all authors")
    void shouldListAllAuthors() throws Exception {
        var authors = List.of(
            new Author("a1", "Author 1"),
            new Author("a2", "Author 2")
        );

        when(authorService.findAll()).thenReturn(authors);

        var expectedAuthors = List.of(
            new AuthorDto("a1", "Author 1"),
            new AuthorDto("a2", "Author 2")
        );

        mvc.perform(get("/api/v1/authors"))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(expectedAuthors)));
    }
}
