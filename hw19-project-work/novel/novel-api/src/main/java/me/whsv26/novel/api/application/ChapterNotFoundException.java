package me.whsv26.novel.api.application;

import lombok.Getter;
import me.whsv26.novel.api.domain.ChapterId;

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