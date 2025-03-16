package me.whsv26.search.api;

import org.springframework.util.Assert;

import java.util.Optional;

public class Range<T> {

    private final T from;

    private final T to;

    public Range(T from, T to) {
        Assert.isTrue(from != null || to != null, "Unbounded range");
        this.from = from;
        this.to = to;
    }

    public static <T> Optional<Range<T>> of(T from, T to) {
        return from == null && to == null
            ? Optional.empty()
            : Optional.of(new Range<>(from, to));
    }

    public Optional<T> getFrom() {
        return Optional.ofNullable(from);
    }

    public Optional<T> getTo() {
        return Optional.ofNullable(to);
    }
}
