package ru.otus.hw.exceptions;

import lombok.Getter;

@Getter
public class BookNotFoundException extends RuntimeException {

    private final String bookId;

    public BookNotFoundException(String bookId) {
        super("Book is not found");
        this.bookId = bookId;
    }
}
