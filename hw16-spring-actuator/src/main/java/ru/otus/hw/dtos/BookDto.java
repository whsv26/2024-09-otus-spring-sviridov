package ru.otus.hw.dtos;

import java.util.List;
import java.util.stream.Collectors;

public record BookDto(
    long id,
    String title,
    AuthorDto author,
    List<GenreDto> genres
) {

    public String genresAsString() {
        return genres.stream()
            .map(GenreDto::name)
            .collect(Collectors.joining(", "));
    }
}
