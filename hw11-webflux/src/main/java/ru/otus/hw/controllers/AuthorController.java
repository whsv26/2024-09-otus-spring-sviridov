package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.dtos.AuthorDto;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.services.AuthorService;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorMapper authorMapper;

    private final AuthorService authorService;

    @GetMapping("/api/v1/authors")
    public Flux<AuthorDto> listAuthors() {
        return authorService.findAll()
            .map(authorMapper::toDto);
    }
}
