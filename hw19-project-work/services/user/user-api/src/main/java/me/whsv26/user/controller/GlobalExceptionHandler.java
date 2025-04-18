package me.whsv26.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.whsv26.user.model.exception.UserAlreadyExistsException;
import me.whsv26.user.model.exception.UserNotFoundException;
import me.whsv26.user.dto.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorInfo handleException(UserAlreadyExistsException err) {
        return new ErrorInfo(
            "USER_ALREADY_EXISTS",
            "User already exists",
            Map.of("username", err.getUsername())
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorInfo handleException(UserNotFoundException err) {
        return new ErrorInfo(
            "USER_NOT_FOUND",
            "User not found",
            Map.of("userId", err.getUserId())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorInfo handleValidationException(MethodArgumentNotValidException err) {
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
