package me.whsv26.novel.model;

public sealed interface NovelEvent permits NovelCreatedEvent, NovelUpdatedEvent, NovelDeletedEvent {

    String getEventId();

    Type getType();

    enum Type {
        CREATED,
        UPDATED,
        DELETED
    }
}