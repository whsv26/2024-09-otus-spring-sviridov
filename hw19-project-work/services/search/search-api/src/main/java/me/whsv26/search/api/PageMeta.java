package me.whsv26.search.api;

public record PageMeta(
    int page,
    int size,
    int totalPages,
    long totalElements,
    boolean hasNext
) {}
