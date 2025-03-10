package ru.otus.hw.controllers;

import java.util.Map;

public record ErrorInfo(
    String errorCode,
    String errorMessage,
    Map<String, Object> errorDetails
) {
}
