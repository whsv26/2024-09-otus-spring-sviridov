package ru.otus.hw.application;

import lombok.Getter;
import ru.otus.hw.domain.NovelId;

@Getter
public class NovelNotFoundException extends RuntimeException {

    private final NovelId id;

    public NovelNotFoundException(NovelId id) {
        this.id = id;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}