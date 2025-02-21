package ru.otus.hw.exceptions;

import lombok.Getter;

@Getter
public class CommentNotFoundException extends RuntimeException {

    private final Long commentId;

    public CommentNotFoundException(Long commentId) {
        super("Comment is not found");
        this.commentId = commentId;
    }
}
