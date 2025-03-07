package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.domain.Book;
import ru.otus.hw.domain.Comment;
import ru.otus.hw.dtos.CommentDto;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.exceptions.CommentNotFoundException;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    public static final String BACKEND = "database";

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    @Override
    @Transactional(readOnly = true)
    @Retry(name = BACKEND)
    @CircuitBreaker(name = BACKEND)
    @TimeLimiter(name = BACKEND)
    public Optional<CommentDto> findById(Long id) {
        return commentRepository.findById(id)
            .map(commentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    @Retry(name = BACKEND)
    @CircuitBreaker(name = BACKEND)
    @TimeLimiter(name = BACKEND)
    public List<CommentDto> findAllFor(Long bookId) {
        return commentRepository.findByBookId(bookId)
            .stream()
            .map(commentMapper::toDto)
            .toList();
    }

    @Override
    @Transactional
    @CircuitBreaker(name = BACKEND)
    @TimeLimiter(name = BACKEND)
    public CommentDto insert(String text, Long bookId) {
        var book = findBook(bookId);
        var comment = new Comment(null, book, text);
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    @Retry(name = BACKEND)
    @CircuitBreaker(name = BACKEND)
    @TimeLimiter(name = BACKEND)
    public CommentDto update(Long id, String text) {
        var comment = findComment(id);
        comment.setText(text);
        return commentMapper.toDto(commentRepository.save(comment));
    }


    @Override
    @Transactional
    @Retry(name = BACKEND)
    @CircuitBreaker(name = BACKEND)
    @TimeLimiter(name = BACKEND)
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    private Book findBook(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() ->
            new BookNotFoundException(bookId)
        );
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
            new CommentNotFoundException(commentId)
        );
    }
}
