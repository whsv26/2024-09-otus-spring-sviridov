package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> findAllFor(long bookId) {
        var book = findBook(bookId);
        return commentRepository.findAllFor(book);
    }

    @Transactional
    @Override
    public Comment insert(String text, long bookId) {
        var book = findBook(bookId);
        var comment = new Comment(0, book, text);
        return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public Comment update(long id, String text) {
        var comment = findComment(id);
        comment.setText(text);
        return commentRepository.save(comment);
    }


    @Transactional
    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private Book findBook(long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() ->
            new EntityNotFoundException("Book with %d id not found".formatted(bookId))
        );
    }

    private Comment findComment(long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
            new EntityNotFoundException("Comment with %d id not found".formatted(commentId))
        );
    }
}
