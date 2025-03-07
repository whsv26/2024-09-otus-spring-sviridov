package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dtos.AuthorDto;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    public static final String BACKEND = "database";

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    @Override
    @Transactional(readOnly = true)
    @Retry(name = BACKEND)
    @CircuitBreaker(name = BACKEND)
    @TimeLimiter(name = BACKEND)
    public List<AuthorDto> findAll() {
        return authorRepository.findAll()
            .stream()
            .map(authorMapper::toDto)
            .toList();
    }
}
