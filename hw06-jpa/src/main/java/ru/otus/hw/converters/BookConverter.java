package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.dto.BookDto;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookConverter {
    
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public String dtoToString(BookDto book) {
        var genresString = book.genres().stream()
            .map(genreConverter::dtoToString)
            .map("{%s}"::formatted)
            .collect(Collectors.joining(", "));
        return "Id: %d, title: %s, author: {%s}, genres: [%s]".formatted(
            book.id(),
            book.title(),
            authorConverter.dtoToString(book.author()),
            genresString
        );
    }

    public BookDto bookToDto(Book book) {
        return new BookDto(
            book.getId(),
            book.getTitle(),
            authorConverter.authorToDto(book.getAuthor()),
            book.getGenres().stream().map(genreConverter::genreToDto).toList()
        );
    }
}
