package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Comment;
import ru.otus.hw.dto.CommentDto;

@RequiredArgsConstructor
@Component
public class CommentConverter {

    public String dtoToString(CommentDto comment) {
        return "Id: %d, text: %s".formatted(
            comment.id(),
            comment.text()
        );
    }

    public CommentDto commentToDto(Comment comment) {
        return new CommentDto(
            comment.getId(),
            comment.getBook().getId(),
            comment.getText()
        );
    }
}
