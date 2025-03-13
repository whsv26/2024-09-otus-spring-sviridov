package ru.otus.hw.domain;

import java.util.UUID;

public record NovelId(String value) implements ValueObject<String> {

    public static NovelId next() {
        return new NovelId(UUID.randomUUID().toString());
    }
}
