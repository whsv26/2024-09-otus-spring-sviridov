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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dtos.BookDto;
import ru.otus.hw.dtos.BookUpsertDto;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.services.BookService;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookMapper bookMapper;

    private final BookService bookService;

    @GetMapping("/api/v1/books")
    public Flux<BookDto> listBooks() {
        return bookService.findAll()
            .map(bookMapper::toDto);
    }

    @GetMapping("/api/v1/books/{id}")
    public Mono<BookDto> viewBook(@PathVariable("id") String bookId) {
        return bookService.findById(bookId)
            .map(bookMapper::toDto)
            .switchIfEmpty(
                Mono.error(new BookNotFoundException(bookId))
            );
    }

    @PostMapping("/api/v1/books")
    public Mono<BookDto> createBook(
        @Valid
        @RequestBody
        BookUpsertDto bookDto
    ) {
        return bookService
            .insert(bookDto.title(), bookDto.authorId(), bookDto.genreIds())
            .map(bookMapper::toDto);
    }

    @PutMapping("/api/v1/books/{id}")
    public Mono<BookDto> updateBook(
        @PathVariable("id")
        String bookId,
        @Valid
        @RequestBody
        BookUpsertDto bookDto
    ) {
        return bookService
            .update(bookId, bookDto.title(), bookDto.authorId(), bookDto.genreIds())
            .map(bookMapper::toDto);
    }

    @DeleteMapping("/api/v1/books/{id}")
    public Mono<Void> deleteBook(
        @PathVariable("id")
        String bookId
    ) {
        return bookService.deleteById(bookId);
    }
}
