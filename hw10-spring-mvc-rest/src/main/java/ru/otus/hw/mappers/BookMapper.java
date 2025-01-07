package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import ru.otus.hw.dtos.BookDto;
import ru.otus.hw.domain.Book;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDto toDto(Book book);
}
