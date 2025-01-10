package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.domain.Book;
import ru.otus.hw.domain.Comment;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.exceptions.CommentNotFoundException;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    public Mono<Comment> findById(String id) {
        return commentRepository.findById(id);
    }

    @Override
    public Flux<Comment> findAllFor(String bookId) {
        return commentRepository.findByBookId(bookId);
    }

    @Override
    public Mono<Comment> insert(String text, String bookId) {
        return findBook(bookId).flatMap(book -> {
            var comment = new Comment(null, book.getId(), text);
            return commentRepository.save(comment);
        });
    }

    @Override
    public Mono<Comment> update(String id, String text) {
        return findComment(id).flatMap(comment -> {
            comment.setText(text);
            return commentRepository.save(comment);
        });
    }


    @Override
    public Mono<Void> deleteById(String id) {
        return commentRepository.deleteById(id);
    }

    private Mono<Book> findBook(String bookId) {
        return bookRepository.findById(bookId).switchIfEmpty(
            Mono.error(new BookNotFoundException(bookId))
        );
    }

    private Mono<Comment> findComment(String commentId) {
        return commentRepository.findById(commentId).switchIfEmpty(
            Mono.error(new CommentNotFoundException(commentId))
        );
    }
}
