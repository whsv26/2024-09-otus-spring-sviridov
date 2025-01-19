package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import ru.otus.hw.domain.Genre;

public interface GenreService {
    Flux<Genre> findAll();
}
