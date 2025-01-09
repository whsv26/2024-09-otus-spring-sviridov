package ru.otus.hw.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Book {

    @Id
    private String id;

    private String title;

    private Author author;

    private List<Genre> genres;

    public Book(String title, Author author, Genre... genres) {
        this.title = title;
        this.author = author;
        this.genres = Arrays.stream(genres).toList();
    }
}
