package ru.otus.hw.services;

import ru.otus.hw.domain.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> findAll();
}
