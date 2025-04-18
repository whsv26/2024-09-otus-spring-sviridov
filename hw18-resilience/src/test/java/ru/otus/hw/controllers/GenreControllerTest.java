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
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.mappers.GenreMapperImpl;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreController.class)
@Import({SecurityConfiguration.class, GenreMapperImpl.class})
public class GenreControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private GenreService genreService;

    @Test
    @DisplayName("Should list all genres")
    void shouldListAllGenres() throws Exception {
        var expectedGenres = List.of(
            new GenreDto(1, "Genre 1"),
            new GenreDto(2, "Genre 2")
        );

        when(genreService.findAll()).thenReturn(expectedGenres);

        mvc.perform(get("/api/v1/genres").with(user("admin")))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(expectedGenres)));
    }
}
