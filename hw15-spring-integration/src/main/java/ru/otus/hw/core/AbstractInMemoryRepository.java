package ru.otus.hw.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractInMemoryRepository<I, T extends HasId<I>> implements CrudRepository<I, T> {

    private final Map<I, T> store = new ConcurrentHashMap<>(
        initializeWith().stream().collect(Collectors.toMap(HasId::getId, Function.identity()))
    );

    @Override
    public T save(T entity) {
        store.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<T> findById(I id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteById(I id) {
        store.remove(id);
    }

    protected List<T> initializeWith() {
        return Collections.emptyList();
    }
}