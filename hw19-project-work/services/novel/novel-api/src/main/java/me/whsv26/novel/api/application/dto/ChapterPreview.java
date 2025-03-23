package me.whsv26.novel.api.application.dto;

import me.whsv26.novel.api.domain.valueobject.ChapterId;

public record ChapterPreview(
    ChapterId id,
    String title
) {}
