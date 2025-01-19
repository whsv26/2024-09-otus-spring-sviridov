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
import ru.otus.hw.domain.Author;
import ru.otus.hw.dtos.AuthorDto;
import ru.otus.hw.services.AuthorService;
import java.util.List;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "mongock.enabled=false")
public class AuthorControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private AuthorService authorService;

    @Test
    @DisplayName("Should list all authors")
    void shouldListAllAuthors() {
        var authors = List.of(
            new Author("a1", "Author 1"),
            new Author("a2", "Author 2")
        );

        when(authorService.findAll()).thenReturn(Flux.fromIterable(authors));

        var expectedAuthors = List.of(
            new AuthorDto("a1", "Author 1"),
            new AuthorDto("a2", "Author 2")
        );

        var actualAuthors = client
            .get().uri("/api/v1/authors")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .returnResult(AuthorDto.class)
            .getResponseBody();

        StepVerifier.create(actualAuthors)
            .expectNextSequence(expectedAuthors)
            .verifyComplete();
    }
}
