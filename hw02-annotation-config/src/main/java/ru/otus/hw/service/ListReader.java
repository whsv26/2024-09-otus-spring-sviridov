package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class ListReader<T> implements Reader<List<T>> {

    private final Reader<T> reader;

    private final char separator;

    @Override
    public List<T> read(String text) {
        var textParts = text.split(String.valueOf(separator));
        return Arrays.stream(textParts)
            .map(String::trim)
            .map(reader::read)
            .toList();
    }

    public static <A> ListReader<A> create(Reader<A> reader, char separator) {
        return new ListReader<>(reader, separator);
    }
}
