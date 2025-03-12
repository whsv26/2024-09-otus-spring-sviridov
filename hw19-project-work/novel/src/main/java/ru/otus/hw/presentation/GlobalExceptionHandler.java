package ru.otus.hw.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.hw.application.NovelNotFoundException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NovelNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorInfo handleException(NovelNotFoundException err) {
        return new ErrorInfo(
            "NOVEL_NOT_FOUND",
            err.getMessage(),
            Map.of("id", err.getId())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorInfo handleException(MethodArgumentNotValidException err) {
        var fieldErrors = err.getBindingResult().getFieldErrors()
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
    public ErrorInfo handleException(Exception err) {
        log.error("Unhandled exception", err);
        return new ErrorInfo(
            "INTERNAL_SERVER_ERROR",
            "Internal Server Error",
            Map.of()
        );
    }

}
