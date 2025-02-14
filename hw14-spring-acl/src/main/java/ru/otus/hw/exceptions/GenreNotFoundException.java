package ru.otus.hw.exceptions;

import lombok.Getter;

@Getter
public class GenreNotFoundException extends RuntimeException {

    private final String genreId;

    public GenreNotFoundException(String genreId) {
        super("Genre is not found");
        this.genreId = genreId;
    }
}
