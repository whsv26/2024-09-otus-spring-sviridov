package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.domain.Genre;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreDto toDto(Genre genre);
}
