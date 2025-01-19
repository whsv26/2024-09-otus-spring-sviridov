package ru.otus.hw.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record BookFormDto (
    @NotBlank
    @Size(max = 255)
    String title,
    @NotBlank
    String authorId,
    @NotNull
    @Size(min = 1)
    Set<String> genreIds
) {
}
