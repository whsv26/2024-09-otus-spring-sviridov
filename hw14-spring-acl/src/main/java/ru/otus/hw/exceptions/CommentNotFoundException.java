package ru.otus.hw.exceptions;

import lombok.Getter;

@Getter
public class CommentNotFoundException extends RuntimeException {

    private final String commentId;

    public CommentNotFoundException(String commentId) {
        super("Comment is not found");
        this.commentId = commentId;
    }
}
