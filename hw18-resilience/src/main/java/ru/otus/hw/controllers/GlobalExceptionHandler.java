package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.hw.exceptions.AuthorNotFoundException;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.exceptions.CommentNotFoundException;
import ru.otus.hw.exceptions.GenreNotFoundException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorInfo handleValidationException(MethodArgumentNotValidException ex) {
        var fieldErrors = ex.getBindingResult().getFieldErrors()
            .stream()
            .collect(Collectors.groupingBy(
                FieldError::getField,
                Collectors.mapping(FieldError::getDefaultMessage, Collectors.toSet())
            ));

        return new ErrorInfo(
            "VALIDATION_ERROR",
            "Validation failed for one or more fields",
            new LinkedHashMap<>(fieldErrors)
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorInfo handleException(Exception ex) {

        log.error("Unhandled exception", ex);

        return new ErrorInfo(
            "INTERNAL_SERVER_ERROR",
            "Internal Server Error",
            Map.of()
        );
    }

}
