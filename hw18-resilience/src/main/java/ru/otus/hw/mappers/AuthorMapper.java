package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import ru.otus.hw.dtos.AuthorDto;
import ru.otus.hw.domain.Author;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorDto toDto(Author author);
}
