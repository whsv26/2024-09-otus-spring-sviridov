package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JDBC для работы с комментариями книг ")
@DataJpaTest
public class JpaCommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен находить комментарии книги")
    @Test
    void shouldFindCommentsByBook() {
        var bookId = 1L;
        var book = em.find(Book.class, bookId);
        var actualComments = commentRepository.findByBookId(bookId);
        var expectedComments = List.of(
            new Comment(1, book, "comment_1"),
            new Comment(2, book, "comment_2")
        );

        assertThat(actualComments)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrderElementsOf(expectedComments);
    }
}
