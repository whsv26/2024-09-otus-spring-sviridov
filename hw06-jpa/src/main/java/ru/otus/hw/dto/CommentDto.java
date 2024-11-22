package ru.otus.hw.dto;

public record CommentDto(
    long id,
    long bookId,
    String text
) {
}
