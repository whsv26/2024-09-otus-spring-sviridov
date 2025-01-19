package ru.otus.hw.exceptions;

public class BookNotFoundException extends EntityNotFoundException {
    public BookNotFoundException(String bookId) {
        super("Book with id " + bookId + " not found");
    }
}
