package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.hw.exceptions.AuthorNotFoundException;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.exceptions.CommentNotFoundException;
import ru.otus.hw.exceptions.GenreNotFoundException;

import java.util.Map;

@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorInfo handleAuthorNotFoundException(AuthorNotFoundException ex) {
        return new ErrorInfo(
            "AUTHOR_NOT_FOUND",
            ex.getMessage(),
            Map.of("id", ex.getAuthorId())
        );
    }

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorInfo handleBookNotFoundException(BookNotFoundException ex) {
        return new ErrorInfo(
            "BOOK_NOT_FOUND",
            ex.getMessage(),
            Map.of("id", ex.getBookId())
        );
    }

    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorInfo handleCommentNotFoundException(CommentNotFoundException ex) {
        return new ErrorInfo(
            "COMMENT_NOT_FOUND",
            ex.getMessage(),
            Map.of("id", ex.getCommentId())
        );
    }

    @ExceptionHandler(GenreNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorInfo handleGenreNotFoundException(GenreNotFoundException ex) {
        return new ErrorInfo(
            "GENRE_NOT_FOUND",
            ex.getMessage(),
            Map.of("id", ex.getGenreId())
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorInfo handeException(Exception ex) {

        log.error("Unhandled exception", ex);

        return new ErrorInfo(
            "INTERNAL_SERVER_ERROR",
            "Internal Server Error",
            Map.of()
        );
    }

}
