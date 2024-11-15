package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JDBC для работы с жанрами книг ")
@DataJpaTest
@Import(JpaGenreRepository.class)
public class JpaGenreRepositoryTest {

    @Autowired
    private JpaGenreRepository genreRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        var expectedGenres = getDbGenres();
        var actualGenres = genreRepository.findAll();
        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
    }

    @DisplayName("должен загружать список жанров по их идентификаторам")
    @Test
    void shouldReturnCorrectGenresListByIds() {
        var genreIds = Set.of(1L, 3L);
        var expectedGenres = genreIds.stream().map(id -> em.find(Genre.class, id)).toList();
        var actualGenres = genreRepository.findAllByIds(genreIds);

        assertThat(actualGenres).containsExactlyInAnyOrderElementsOf(expectedGenres);
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
            .map(id -> new Genre(id, "Genre_" + id))
            .toList();
    }
}
