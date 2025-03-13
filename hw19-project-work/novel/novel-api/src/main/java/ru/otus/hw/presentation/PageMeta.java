package ru.otus.hw.presentation;

public record PageMeta(
    int page,
    int size,
    int totalPages,
    long totalElements,
    boolean hasNext
) {}
