package me.whsv26.novel.api.application;

import me.whsv26.novel.api.domain.ChapterId;

public record ChapterPreview(
    ChapterId id,
    String title
) {}
