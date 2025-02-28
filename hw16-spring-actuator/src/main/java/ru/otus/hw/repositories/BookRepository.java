package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.domain.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
