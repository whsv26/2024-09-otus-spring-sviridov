package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    public static final String BACKEND = "database";

    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    @Override
    @Transactional(readOnly = true)
    @Retry(name = BACKEND)
    @CircuitBreaker(name = BACKEND)
    @TimeLimiter(name = BACKEND)
    public List<GenreDto> findAll() {
        return genreRepository.findAll()
            .stream()
            .map(genreMapper::toDto)
            .toList();
    }
}
