package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Author;
import ru.otus.hw.dto.AuthorDto;

@Component
public class AuthorConverter {

    public String dtoToString(AuthorDto author) {
        return "Id: %d, FullName: %s".formatted(author.id(), author.fullName());
    }

    public AuthorDto authorToDto(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }
}
