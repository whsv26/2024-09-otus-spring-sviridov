package ru.otus.hw.marketing.domain;

import java.util.UUID;

public record ProductId(
    String value
) {

    public static ProductId next() {
        return new ProductId(UUID.randomUUID().toString());
    }
}
