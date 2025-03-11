package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.AbstractIntegrationTest;
import ru.otus.hw.dtos.CommentDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с комментариями книг ")
@SpringBootTest
class CommentServiceImplTest extends AbstractIntegrationTest {

    @Autowired
    private CommentServiceImpl commentService;

    @DisplayName("должен находить комментарий по его id")
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void shouldReturnCommentById() {
        var expectedComment = new CommentDto(1, 1, "comment_1");
        var comment =  commentService.findById(1L);

        assertThat(comment)
            .isPresent()
            .get()
            .isEqualTo(expectedComment);
    }

    @DisplayName("должен находить все комментарии книги")
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void shouldReturnAllBookComments() {
        var bookId = 1L;
        var expectedComments = List.of(
            new CommentDto(1, bookId, "comment_1"),
            new CommentDto(2, bookId, "comment_2")
        );
        var actualComments =  commentService.findAllFor(bookId);

        assertThat(actualComments).containsExactlyInAnyOrderElementsOf(expectedComments);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Transactional
    @Test
    void shouldSaveNewComment() {
        var bookId = 1L;
        var text = "new_comment";
        var expectedComment = new CommentDto(3, bookId, text);
        var actualComment = commentService.insert(text, bookId);

        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @DisplayName("должен обновлять текст комментария")
    @Transactional
    @Test
    void shouldUpdateCommentText() {
        var bookId = 1L;
        var text = "comment_1_updated";
        var expectedComment = new CommentDto(1, bookId, text);
        var actualComment = commentService.update(1L, text);
        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @DisplayName("должен удалять комментарий по id")
    @Transactional
    @Test
    void deleteCommentById() {
        commentService.deleteById(1L);
        var actualComment = commentService.findById(1L);
        assertThat(actualComment).isEmpty();
    }
}