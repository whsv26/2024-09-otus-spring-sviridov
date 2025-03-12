package ru.otus.hw.application;

import lombok.Getter;
import ru.otus.hw.domain.ChapterId;

@Getter
public class ChapterNotFoundException extends RuntimeException {

    private final ChapterId id;

    public ChapterNotFoundException(ChapterId id) {
        this.id = id;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}