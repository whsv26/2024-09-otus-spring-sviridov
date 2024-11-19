package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с комментариями книг ")
@SpringBootTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class CommentServiceImplTest {

    @Autowired
    private CommentServiceImpl commentService;

    @DisplayName("должен находить комментарий по его id")
    @Test
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
    @DirtiesContext
    @Test
    void shouldSaveNewComment() {
        var bookId = 1L;
        var text = "new_comment";
        var expectedComment = new CommentDto(3, bookId, text);
        var actualComment = commentService.insert(text, bookId);

        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @DisplayName("должен обновлять текст комментария")
    @DirtiesContext
    @Test
    void shouldUpdateCommentText() {
        var bookId = 1L;
        var text = "comment_1_updated";
        var expectedComment = new CommentDto(1, bookId, text);
        var actualComment = commentService.update(1, text);
        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @DisplayName("должен удалять комментарий по id")
    @DirtiesContext
    @Test
    void deleteCommentById() {
        commentService.deleteById(1);
        var actualComment = commentService.findById(1);
        assertThat(actualComment).isEmpty();
    }
}