package ru.otus.hw.domain;

import java.util.UUID;

public record ChapterId(String value) implements ValueObject<String> {

    public static ChapterId next() {
        return new ChapterId(UUID.randomUUID().toString());
    }
}
