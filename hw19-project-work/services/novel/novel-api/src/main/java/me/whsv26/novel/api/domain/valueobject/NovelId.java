package me.whsv26.novel.api.domain.valueobject;

import java.util.UUID;

public record NovelId(String value) implements ValueObject<String> {

    public static NovelId next() {
        return new NovelId(UUID.randomUUID().toString());
    }
}
