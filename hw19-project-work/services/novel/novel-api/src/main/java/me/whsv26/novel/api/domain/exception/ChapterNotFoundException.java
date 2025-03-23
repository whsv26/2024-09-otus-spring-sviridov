package me.whsv26.novel.api.domain.exception;

import lombok.Getter;
import me.whsv26.novel.api.domain.valueobject.ChapterId;

@Getter
public class ChapterNotFoundException extends RuntimeException {

    private final ChapterId id;

    public ChapterNotFoundException(ChapterId id) {
        super("Chapter not found");
        this.id = id;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}