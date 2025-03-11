package ru.otus.hw.exceptions;

import lombok.Getter;

@Getter
public class GenreNotFoundException extends RuntimeException {

    private final Long genreId;

    public GenreNotFoundException(Long genreId) {
        super("Genre is not found");
        this.genreId = genreId;
    }
}
