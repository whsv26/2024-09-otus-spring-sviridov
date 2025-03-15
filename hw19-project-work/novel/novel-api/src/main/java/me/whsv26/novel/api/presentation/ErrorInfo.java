package me.whsv26.novel.api.presentation;

import java.util.Map;

public record ErrorInfo(
    String errorCode,
    String errorMessage,
    Map<String, Object> errorDetails
) {
}
