package ru.otus.hw.application;

import java.time.LocalDateTime;
import java.util.List;

public record NovelEvent(
    String id,
    String novelId,
    String title,
    String synopsis,
    String authorId,
    List<String>genres,
    List<String> tags,
    LocalDateTime createdAt
) {}
