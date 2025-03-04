package ru.otus.hw.core;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<I, T> {
    
    T save(T entity);

    Optional<T> findById(I id);

    List<T> findAll();

    void deleteById(I id);
}
