package ru.otus.hw.exceptions;

import lombok.Getter;

@Getter
public class AuthorNotFoundException extends RuntimeException {

    private final String authorId;

    public AuthorNotFoundException(String authorId) {
        super("Author is not found");
        this.authorId = authorId;
    }
}
