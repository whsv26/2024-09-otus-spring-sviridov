package me.whsv26.novel.api.domain.valueobject;

import java.util.List;

public record CreateNovelDetails(
    NovelId id,
    String title,
    String synopsis,
    AuthorId authorId,
    List<GenreId> genres,
    List<String> tags
) {}
