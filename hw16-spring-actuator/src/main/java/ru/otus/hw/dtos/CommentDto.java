package ru.otus.hw.dtos;

public record CommentDto(
    long id,
    long bookId,
    String text
) {
}
