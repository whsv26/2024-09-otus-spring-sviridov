package me.whsv26.novel.api.domain;

import java.util.UUID;

public record ChapterId(String value) implements ValueObject<String> {

    public static ChapterId next() {
        return new ChapterId(UUID.randomUUID().toString());
    }
}
