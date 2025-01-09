package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.domain.Author;
import ru.otus.hw.domain.Book;
import ru.otus.hw.domain.Comment;
import ru.otus.hw.domain.Genre;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с комментариями книг ")
@SpringBootTest
class CommentServiceImplTest {

    @Autowired
    private CommentServiceImpl commentService;

    @DisplayName("должен находить комментарий по его id")
    @Test
    void shouldReturnCommentById() {
        var expectedAuthor = new Author("1", "Author_1");
        var expectedGenres = List.of(
            new Genre("1", "Genre_1"),
            new Genre("2", "Genre_2")
        );
        var expectedBook = new Book("1", "BookTitle_1", expectedAuthor, expectedGenres);
        var expectedComment = new Comment("1", expectedBook, "comment_1");
        var comment =  commentService.findById("1");

        assertThat(comment)
            .isPresent()
            .get()
            .isEqualTo(expectedComment);
    }

    @DisplayName("должен находить все комментарии книги")
    @Test
    void shouldReturnAllBookComments() {
        var bookId = "1";
        var expectedAuthor = new Author("1", "Author_1");
        var expectedGenres = List.of(
            new Genre("1", "Genre_1"),
            new Genre("2", "Genre_2")
        );
        var expectedBook = new Book(bookId, "BookTitle_1", expectedAuthor, expectedGenres);
        var expectedComments = List.of(
            new Comment("1", expectedBook, "comment_1"),
            new Comment("2", expectedBook, "comment_2")
        );
        var actualComments =  commentService.findAllFor(bookId);

        assertThat(actualComments).containsExactlyInAnyOrderElementsOf(expectedComments);
    }

    @DisplayName("должен сохранять новый комментарий")
    @DirtiesContext
    @Test
    void shouldSaveNewComment() {
        var bookId = "1";
        var expectedAuthor = new Author("1", "Author_1");
        var expectedGenres = List.of(
            new Genre("1", "Genre_1"),
            new Genre("2", "Genre_2")
        );
        var expectedBook = new Book(bookId, "BookTitle_1", expectedAuthor, expectedGenres);
        var text = "new_comment";
        var expectedComment = new Comment("3", expectedBook, text);
        var actualComment = commentService.insert(text, bookId);

        assertThat(actualComment)
            .matches(comment -> Objects.nonNull(comment.getId()))
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(expectedComment);
    }

    @DisplayName("должен обновлять текст комментария")
    @DirtiesContext
    @Test
    void shouldUpdateCommentText() {
        var bookId = "1";
        var expectedAuthor = new Author("1", "Author_1");
        var expectedGenres = List.of(
            new Genre("1", "Genre_1"),
            new Genre("2", "Genre_2")
        );
        var expectedBook = new Book(bookId, "BookTitle_1", expectedAuthor, expectedGenres);
        var text = "comment_1_updated";
        var commentId = "1";
        var expectedComment = new Comment(commentId, expectedBook, text);
        var actualComment = commentService.update(commentId, text);
        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @DisplayName("должен удалять комментарий по id")
    @DirtiesContext
    @Test
    void deleteCommentById() {
        var commentId = "1";
        commentService.deleteById(commentId);
        var actualComment = commentService.findById(commentId);
        assertThat(actualComment).isEmpty();
    }
}