package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dtos.BookFormDto;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.Collections;

@Controller
@Slf4j
@RequiredArgsConstructor
public class BookController {

    private final BookMapper bookMapper;

    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/books")
    public String listBooks(Model model) {
        var books = bookService.findAll().stream().map(bookMapper::toDto).toList();
        model.addAttribute("books", books);
        return "listBooks";
    }

    @GetMapping("/books/create")
    public String createBookPage(Model model) {
        var genres = genreService.findAll().stream().map(genreMapper::toDto).toList();
        var authors = authorService.findAll().stream().map(authorMapper::toDto).toList();
        var book = new BookFormDto(null, null, Collections.emptySet());
        model.addAttribute("genres", genres);
        model.addAttribute("authors", authors);
        model.addAttribute("book", book);
        return "createBook";
    }

    @PostMapping("/books/create")
    public String createBook(
        @Valid
        @ModelAttribute("book")
        BookFormDto book,
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            var genres = genreService.findAll().stream().map(genreMapper::toDto).toList();
            var authors = authorService.findAll().stream().map(authorMapper::toDto).toList();
            model.addAttribute("genres", genres);
            model.addAttribute("authors", authors);
            return "createBook";
        }

        bookService.insert(
            book.title(),
            book.authorId(),
            book.genreIds()
        );

        return "redirect:/books";
    }

    @GetMapping("/books/edit/{id}")
    public String editBookPage(
        @PathVariable("id") String bookId,
        Model model
    ) {
        var genres = genreService.findAll().stream().map(genreMapper::toDto).toList();
        var authors = authorService.findAll().stream().map(authorMapper::toDto).toList();
        var book = bookService.findById(bookId)
            .orElseThrow(() -> new BookNotFoundException(bookId));
        model.addAttribute("genres", genres);
        model.addAttribute("authors", authors);
        model.addAttribute("book", bookMapper.toForm(book));
        model.addAttribute("bookId", bookId);
        return "editBook";
    }

    @PostMapping("/books/edit/{id}")
    public String editBook(
        @PathVariable("id") String bookId,
        @Valid
        @ModelAttribute("book")
        BookFormDto book,
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            var genres = genreService.findAll().stream().map(genreMapper::toDto).toList();
            var authors = authorService.findAll().stream().map(authorMapper::toDto).toList();
            model.addAttribute("genres", genres);
            model.addAttribute("authors", authors);
            model.addAttribute("bookId", bookId);
            return "editBook";
        }

        bookService.update(
            bookId,
            book.title(),
            book.authorId(),
            book.genreIds()
        );

        return "redirect:/books";
    }

    @PostMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable("id") String bookId) {
        bookService.deleteById(bookId);
        return "redirect:/books";
    }
}
