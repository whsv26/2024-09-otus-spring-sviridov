package me.whsv26.novel.api.application;

import lombok.Getter;
import me.whsv26.novel.api.domain.NovelId;

@Getter
public class NovelNotFoundException extends RuntimeException {

    private final NovelId id;

    public NovelNotFoundException(NovelId id) {
        super("Novel not found");
        this.id = id;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}