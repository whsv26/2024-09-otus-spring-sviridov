package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BoundedIntReader implements Reader<Integer> {

    private final int min;

    private final int max;

    @Override
    public Integer read(String text) {
        var intValue = Integer.parseInt(text);

        if (intValue < min || intValue > max) {
            var error = "%d is out of range [%d, %d]";
            throw new IllegalArgumentException(error.formatted(intValue, min, max));
        }

        return intValue;
    }
}
