package ru.otus.hw.exceptions;

import lombok.Getter;

import java.util.Set;

@Getter
public class GenresNotFoundException extends RuntimeException {

    private final Set<String> genreIds;

    public GenresNotFoundException(Set<String> genreIds) {
        super("Genres are not found");
        this.genreIds = genreIds;
    }
}
