package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.hw.domain.Comment;
import ru.otus.hw.dtos.CommentDto;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "book.id", target = "bookId")
    CommentDto toDto(Comment genre);
}
