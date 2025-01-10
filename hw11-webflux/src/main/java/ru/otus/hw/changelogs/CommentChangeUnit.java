package ru.otus.hw.changelogs;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import ru.otus.hw.domain.Book;
import ru.otus.hw.domain.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.Comparator;
import java.util.function.Function;

@RequiredArgsConstructor
@ChangeUnit(id = "commentChangeUnitId", order = "004", runAlways = true)
public class CommentChangeUnit {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Execution
    public void initComments() {
        bookRepository.findAll()
            .sort(Comparator.comparing(Book::getId))
            .collectList()
            .map(books ->
                Flux.range(1, 2)
                    .map(i -> new Comment(String.valueOf(i), books.get(0).getId(),"comment_" + i))
                    .flatMap(commentRepository::save)
            )
            .flux()
            .flatMap(Function.identity())
            .blockLast();
    }

    @RollbackExecution
    public void rollback() {
        Flux.from(commentRepository.deleteAll()).blockLast();
    }
}
