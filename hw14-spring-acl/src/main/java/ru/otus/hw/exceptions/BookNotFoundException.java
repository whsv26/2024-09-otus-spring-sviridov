package ru.otus.hw.exceptions;

import lombok.Getter;

@Getter
public class BookNotFoundException extends RuntimeException {

    private final Long bookId;

    public BookNotFoundException(Long bookId) {
        super("Book is not found");
        this.bookId = bookId;
    }
}
