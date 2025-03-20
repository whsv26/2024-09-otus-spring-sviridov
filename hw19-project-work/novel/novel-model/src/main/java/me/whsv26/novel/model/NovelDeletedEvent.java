package me.whsv26.novel.model;

import java.time.LocalDateTime;

public record NovelDeletedEvent(
    String eventId,
    String novelId,
    LocalDateTime occuredAt
) implements NovelEvent {

    @Override
    public String getEventId() {
        return eventId;
    }
}
