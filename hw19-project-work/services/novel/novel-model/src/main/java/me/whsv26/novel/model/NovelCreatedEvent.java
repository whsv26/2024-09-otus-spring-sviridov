package me.whsv26.novel.model;

import java.time.LocalDateTime;
import java.util.List;

public record NovelCreatedEvent(
    String eventId,
    String novelId,
    String title,
    String synopsis,
    String authorId,
    List<String> genres,
    List<String> tags,
    LocalDateTime occuredAt
) implements NovelEvent {

    @Override
    public String getEventId() {
        return eventId;
    }

    @Override
    public Type getType() {
        return Type.CREATED;
    }
}
