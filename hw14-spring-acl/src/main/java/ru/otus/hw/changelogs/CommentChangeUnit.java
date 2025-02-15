package ru.otus.hw.changelogs;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.domain.Book;
import ru.otus.hw.domain.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.Comparator;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@ChangeUnit(id = "commentChangeUnitId", order = "004", runAlways = true)
public class CommentChangeUnit {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Execution
    public void initComments() {
        var books = bookRepository.findAll();
        books.sort(Comparator.comparing(Book::getId));

        IntStream.range(1, 3)
            .mapToObj(i -> new Comment(String.valueOf(i), books.get(0),"comment_" + i))
            .forEach(commentRepository::save);
    }

    @RollbackExecution
    public void rollback() {
        commentRepository.deleteAll();
    }
}
