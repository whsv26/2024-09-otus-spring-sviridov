package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    @Override
    public Optional<Comment> findById(String id) {
        return commentRepository.findById(id);
    }

    @Override
    public List<Comment> findAllFor(String bookId) {
        return commentRepository.findByBookId(bookId)
            .stream()
            .toList();
    }

    @Override
    public Comment insert(String text, String bookId) {
        var book = findBook(bookId);
        var comment = new Comment(null, book, text);
        return commentRepository.save(comment);
    }

    @Override
    public Comment update(String id, String text) {
        var comment = findComment(id);
        comment.setText(text);
        return commentRepository.save(comment);
    }


    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    private Book findBook(String bookId) {
        return bookRepository.findById(bookId).orElseThrow(() ->
            new EntityNotFoundException("Book with %s id not found".formatted(bookId))
        );
    }

    private Comment findComment(String commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
            new EntityNotFoundException("Comment with %s id not found".formatted(commentId))
        );
    }
}
