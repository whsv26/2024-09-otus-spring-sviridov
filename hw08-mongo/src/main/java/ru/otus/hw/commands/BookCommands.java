package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.services.BookService;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class BookCommands {

    private final BookService bookService;

    private final BookConverter bookConverter;

    @ShellMethod(value = "Find all books", key = {"find-all-books", "ab"})
    public String findAllBooks() {
        return bookService.findAll().stream()
            .map(bookConverter::bookToString)
            .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find book by id", key = {"find-book-by-id", "bbid"})
    public String findBookById(String id) {
        return bookService.findById(id)
            .map(bookConverter::bookToString)
            .orElse("Book with id %s not found".formatted(id));
    }

    @ShellMethod(value = "Insert book", key = {"insert-book", "bins"})
    public String insertBook(String title, String authorId, Set<String> genresIds) {
        var savedBook = bookService.insert(title, authorId, genresIds);
        return bookConverter.bookToString(savedBook);
    }

    @ShellMethod(value = "Update book", key = {"update-book", "bupd"})
    public String updateBook(String id, String title, String authorId, Set<String> genresIds) {
        var savedBook = bookService.update(id, title, authorId, genresIds);
        return bookConverter.bookToString(savedBook);
    }

    @ShellMethod(value = "Delete book by id", key = {"delete-book", "bdel"})
    public void deleteBook(String id) {
        bookService.deleteById(id);
    }
}
