package me.whsv26.novel.api.presentation.dto;

import java.util.Map;

public record ErrorInfo(
    String errorCode,
    String errorMessage,
    Map<String, Object> errorDetails
) {
}
