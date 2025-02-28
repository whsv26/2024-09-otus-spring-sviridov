package ru.otus.hw.indicators;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.BookRepository;

@Component
@RequiredArgsConstructor
public class BookHealthIndicator implements HealthIndicator {

    private static final int MIN_HEALTHY_BOOK_COUNT = 3;

    private final BookRepository bookRepository;

    @Override
    public Health health() {
        var booksCount = bookRepository.count();

        if (booksCount < MIN_HEALTHY_BOOK_COUNT) {
            return Health.down()
                .withDetail("currentBooksCount", booksCount)
                .withDetail("healthyBooksCount", MIN_HEALTHY_BOOK_COUNT)
                .build();
        } else {
            return Health.up()
                .withDetail("currentBooksCount", booksCount)
                .withDetail("healthyBooksCount", MIN_HEALTHY_BOOK_COUNT)
                .build();
        }
    }
}
