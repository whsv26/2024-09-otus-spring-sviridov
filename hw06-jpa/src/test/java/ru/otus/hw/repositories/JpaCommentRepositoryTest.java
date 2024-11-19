package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JDBC для работы с комментариями книг ")
@DataJpaTest
@Import(JpaCommentRepository.class)
public class JpaCommentRepositoryTest {

    @Autowired
    private JpaCommentRepository commentRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен находить комментарий по id")
    @Test
    void shouldReturnCommentById() {
        var expectedBook = em.find(Book.class, 1);
        var expectedComment = new Comment(1, expectedBook, "comment_1");
        var actualComment = commentRepository.findById(expectedComment.getId());

        assertThat(actualComment)
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(expectedComment);
    }

    @DisplayName("должен находить комментарии книги")
    @Test
    void shouldFindCommentsByBook() {
        var bookId = 1L;
        var book = em.find(Book.class, bookId);
        var actualComments = commentRepository.findAllFor(book);
        var expectedComments = List.of(
            new Comment(1, book, "comment_1"),
            new Comment(2, book, "comment_2")
        );

        assertThat(actualComments)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrderElementsOf(expectedComments);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var bookId = 1L;
        var book = em.find(Book.class, bookId);
        var comment = new Comment(0, book, "comment");
        commentRepository.save(comment);
        var actualComment = em.find(Comment.class, comment.getId());

        assertThat(actualComment).isNotNull();
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        var commentId = 1L;
        var comment = em.find(Comment.class, commentId);
        comment.setText(comment.getText() + "_updated");
        commentRepository.save(comment);
        var actualComment = em.find(Comment.class, comment.getId());
        assertThat(actualComment.getText()).isEqualTo("comment_1_updated");
    }

    @DisplayName("должен удалять комментарий по id")
    @Test
    void shouldDeleteComment() {
        var commentId = 1L;
        var comment = em.find(Comment.class, commentId);
        assertThat(comment).isNotNull();
        em.detach(comment);
        commentRepository.deleteById(commentId);
        assertThat(em.find(Comment.class, commentId)).isNull();
    }

}
