package me.whsv26.novel.api.presentation;

public record PageMeta(
    int page,
    int size,
    int totalPages,
    long totalElements,
    boolean hasNext
) {}
