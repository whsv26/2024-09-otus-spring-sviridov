package me.whsv26.rating.model;

public record NovelRatingCommand(
    String commandId,
    String novelId,
    String userId,
    int rating
) {}
