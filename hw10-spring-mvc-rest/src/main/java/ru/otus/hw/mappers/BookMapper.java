package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.hw.dtos.BookDto;
import ru.otus.hw.dtos.BookFormDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDto toDto(Book book);

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "genres", target = "genreIds")
    BookFormDto toForm(Book book);

    default Set<String> mapGenresToIds(List<Genre> genres) {
        if (genres == null) {
            return null;
        }
        return genres.stream()
            .map(Genre::getId)
            .collect(Collectors.toSet());
    }
}
