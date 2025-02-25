package ru.otus.hw.marketing;

import java.util.UUID;

public record ProductId(
    String value
) {

    static ProductId next() {
        return new ProductId(UUID.randomUUID().toString());
    }
}
