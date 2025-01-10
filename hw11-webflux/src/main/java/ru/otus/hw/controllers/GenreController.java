package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.services.GenreService;

@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreMapper genreMapper;

    private final GenreService genreService;

    @GetMapping("/api/v1/genres")
    public Flux<GenreDto> listGenres() {
        return genreService.findAll()
            .map(genreMapper::toDto);
    }
}
