package ru.otus.hw.application;

import ru.otus.hw.domain.ChapterId;

public record ChapterPreview(
    ChapterId id,
    String title
) {}
