package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @ShellMethod(value = "Find all book comments", key = {"find-all-book-comments", "abc"})
    public String findAllBookComments(long bookId) {
        return commentService.findAllFor(bookId).stream()
            .map(commentConverter::dtoToString)
            .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find comment by id", key = {"find-comment-by-id", "cbid"})
    public String findCommentById(long id) {
        return commentService.findById(id)
            .map(commentConverter::dtoToString)
            .orElse("Comment with id %d not found".formatted(id));
    }

    @ShellMethod(value = "Insert comment", key = {"insert-comment", "cins"})
    public String insertComment(String text, long bookId) {
        var savedComment = commentService.insert(text, bookId);
        return commentConverter.dtoToString(savedComment);
    }

    @ShellMethod(value = "Update comment", key = {"update-comment", "cupd"})
    public String updateComment(long id, String text) {
        var savedComment = commentService.update(id, text);
        return commentConverter.dtoToString(savedComment);
    }

    @ShellMethod(value = "Delete comment by id", key = {"delete-comment", "cdel"})
    public void deleteComment(long id) {
        commentService.deleteById(id);
    }
}
