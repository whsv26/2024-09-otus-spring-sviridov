package ru.otus.hw.service;

public interface Reader<T> {
    T read(String text);
}
