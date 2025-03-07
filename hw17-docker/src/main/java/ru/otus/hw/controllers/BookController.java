package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dtos.BookDto;
import ru.otus.hw.dtos.BookUpsertDto;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/api/v1/books")
    public List<BookDto> listBooks() {
        return bookService.findAll().stream().toList();
    }

    @GetMapping("/api/v1/books/{id}")
    public BookDto viewBook(@PathVariable("id") Long bookId) {
        return bookService.findById(bookId)
            .orElseThrow(() -> new BookNotFoundException(bookId));
    }

    @PostMapping("/api/v1/books")
    public BookDto createBook(
        @Valid
        @RequestBody
        BookUpsertDto bookDto
    ) {
        return bookService.insert(
            bookDto.title(),
            bookDto.authorId(),
            bookDto.genreIds()
        );
    }

    @PutMapping("/api/v1/books/{id}")
    public BookDto updateBook(
        @PathVariable("id")
        Long bookId,
        @Valid
        @RequestBody
        BookUpsertDto bookDto
    ) {
        return bookService.update(
            bookId,
            bookDto.title(),
            bookDto.authorId(),
            bookDto.genreIds()
        );
    }

    @DeleteMapping("/api/v1/books/{id}")
    public void deleteBook(
        @PathVariable("id")
        Long bookId
    ) {
        bookService.deleteById(bookId);
    }
}
