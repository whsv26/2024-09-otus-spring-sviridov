package ru.otus.hw.repositories;

public interface KeyMappingRepository<S, T> {

    T get(S sourceKey, Class<?> type);

    void set(S sourceKey, T targetKey, Class<?> type);
}
