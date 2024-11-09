package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JDBC для работы с авторами книг ")
@JdbcTest
@Import(JdbcAuthorRepository.class)
public class JdbcAuthorRepositoryTest {

    @Autowired
    private JdbcAuthorRepository authorRepository;

    @DisplayName("должен загружать список всех авторов")
    @Test
    void shouldReturnAuthorsList() {
        var expectedAuthors = getDbAuthors();
        var actualAuthors = authorRepository.findAll();

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
    }

    @DisplayName("должен загружать автора по его идентификатору")
    @Test
    void shouldReturnAuthorById() {
        var authorId = 2L;
        var expectedAuthors = getDbAuthors().stream()
            .filter(author -> author.getId() == authorId)
            .findFirst();
        var actualAuthors = authorRepository.findById(authorId);

        assertThat(actualAuthors).isEqualTo(expectedAuthors);
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
            .map(id -> new Author(id, "Author_" + id))
            .toList();
    }
}
