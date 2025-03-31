package me.whsv26.novel.api.domain.valueobject;

import java.util.List;

public record UpdateNovelDetails(
    String title,
    String synopsis,
    List<GenreId> genres,
    List<String> tags
) {}
