package ru.otus.hw.exceptions;

import lombok.Getter;

@Getter
public class AuthorNotFoundException extends RuntimeException {

    private final Long authorId;

    public AuthorNotFoundException(Long authorId) {
        super("Author is not found");
        this.authorId = authorId;
    }
}
