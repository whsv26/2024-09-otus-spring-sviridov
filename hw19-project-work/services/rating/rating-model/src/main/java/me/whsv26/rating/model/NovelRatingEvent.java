package me.whsv26.rating.model;

public record NovelRatingEvent(
    String eventId,
    String novelId,
    float rating
) {}
