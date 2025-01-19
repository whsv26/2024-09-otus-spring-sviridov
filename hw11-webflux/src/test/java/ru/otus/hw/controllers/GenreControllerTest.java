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
import reactor.test.StepVerifier;
import ru.otus.hw.domain.Genre;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "mongock.enabled=false")
public class GenreControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private GenreService genreService;

    @Test
    @DisplayName("Should list all genres")
    void shouldListAllGenres() {
        var genres = List.of(
            new Genre("g1", "Genre 1"),
            new Genre("g2", "Genre 2")
        );

        when(genreService.findAll()).thenReturn(Flux.fromIterable(genres));

        var expectedGenres = List.of(
            new GenreDto("g1", "Genre 1"),
            new GenreDto("g2", "Genre 2")
        );

        var actualGenres = client
            .get().uri("/api/v1/genres")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .returnResult(GenreDto.class)
            .getResponseBody();

        StepVerifier.create(actualGenres)
            .expectNextSequence(expectedGenres)
            .verifyComplete();
    }
}
