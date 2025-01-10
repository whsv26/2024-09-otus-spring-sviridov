package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import reactor.test.StepVerifier;
import ru.otus.hw.MongockTestConfig;
import ru.otus.hw.domain.Comment;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с комментариями книг ")
@SpringBootTest
@Import(MongockTestConfig.class)
class CommentServiceImplTest {

    @Autowired
    private CommentServiceImpl commentService;

    @DisplayName("должен находить комментарий по его id")
    @Test
    void shouldReturnCommentById() {
        StepVerifier.create(commentService.findById("1"))
            .expectNext(new Comment("1", "1", "comment_1"))
            .verifyComplete();
    }

    @DisplayName("должен находить все комментарии книги")
    @Test
    void shouldReturnAllBookComments() {
        var bookId = "1";
        StepVerifier.create(commentService.findAllFor(bookId).sort(Comparator.comparing(Comment::getId)))
            .expectNext(new Comment("1", bookId, "comment_1"))
            .expectNext(new Comment("2", bookId, "comment_2"))
            .verifyComplete();
    }

    @DisplayName("должен сохранять новый комментарий")
    @DirtiesContext
    @Test
    void shouldSaveNewComment() {
        var bookId = "1";
        var text = "new_comment";
        var expectedComment = new Comment("3", bookId, text);

        StepVerifier.create(commentService.insert(text, bookId))
            .assertNext(comment -> {
                assertThat(comment.getId()).isNotNull();
                assertThat(comment)
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(expectedComment);
            })
            .verifyComplete();
    }

    @DisplayName("должен обновлять текст комментария")
    @DirtiesContext
    @Test
    void shouldUpdateCommentText() {
        var bookId = "1";
        var commentId = "1";
        var text = "comment_1_updated";

        StepVerifier.create(commentService.update(commentId, text))
            .expectNext(new Comment(commentId, bookId, text))
            .verifyComplete();
    }

    @DisplayName("должен удалять комментарий по id")
    @DirtiesContext
    @Test
    void deleteCommentById() {
        var commentId = "1";
        StepVerifier.create(commentService.deleteById(commentId)).verifyComplete();
        StepVerifier.create(commentService.findById(commentId)).verifyComplete();
    }
}