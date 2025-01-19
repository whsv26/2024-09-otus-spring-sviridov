package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;
import ru.otus.hw.MongockTestConfig;
import ru.otus.hw.domain.Comment;

import java.util.Comparator;

@DisplayName("Репозиторий на основе JDBC для работы с комментариями книг ")
@DataMongoTest
@Import(MongockTestConfig.class)
public class MongoCommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @DisplayName("должен находить комментарии книги")
    @Test
    void shouldFindCommentsByBook() {
        var bookId = "1";
        var actualComments = commentRepository.findByBookId(bookId).sort(Comparator.comparing(Comment::getId));

        StepVerifier.create(actualComments)
            .expectNext(new Comment("1", bookId, "comment_1"))
            .expectNext(new Comment("2", bookId, "comment_2"))
            .verifyComplete();
    }
}
