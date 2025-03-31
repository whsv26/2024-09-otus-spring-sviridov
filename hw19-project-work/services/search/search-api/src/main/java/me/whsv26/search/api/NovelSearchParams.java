package me.whsv26.search.api;

import org.springframework.lang.Nullable;

import java.util.List;

public record NovelSearchParams(
    @Nullable String prompt,
    @Nullable String authorName,
    @Nullable Range<Float> ratingRange,
    @Nullable List<String> genres,
    @Nullable List<String> tags
) {}
