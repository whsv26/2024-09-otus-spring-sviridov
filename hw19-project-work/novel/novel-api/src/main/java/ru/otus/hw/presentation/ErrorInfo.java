package ru.otus.hw.presentation;

import java.util.Map;

public record ErrorInfo(
    String errorCode,
    String errorMessage,
    Map<String, Object> errorDetails
) {
}
