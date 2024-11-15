package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с авторами книг ")
@DataJpaTest
@Import(JpaAuthorRepository.class)
public class JpaAuthorRepositoryTest {

    @Autowired
    private JpaAuthorRepository authorRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать список всех авторов")
    @Test
    void shouldReturnAuthorsList() {
        var expectedAuthors = getDbAuthors();
        var actualAuthors = authorRepository.findAll();

        assertThat(actualAuthors).containsExactlyInAnyOrderElementsOf(expectedAuthors);
    }

    @DisplayName("должен загружать автора по его идентификатору")
    @Test
    void shouldReturnAuthorById() {
        var authorId = 2L;
        var expectedAuthor = em.find(Author.class, authorId);
        var actualAuthor = authorRepository.findById(authorId);

        assertThat(actualAuthor).isEqualTo(Optional.ofNullable(expectedAuthor));
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
            .map(id -> new Author(id, "Author_" + id))
            .toList();
    }
}
