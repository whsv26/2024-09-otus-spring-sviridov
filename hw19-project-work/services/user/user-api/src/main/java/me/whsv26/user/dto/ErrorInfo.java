package me.whsv26.user.dto;

import java.util.Map;

public record ErrorInfo(
    String errorCode,
    String errorMessage,
    Map<String, Object> errorDetails
) {
}
