package ru.otus.hw.order.domain;

import java.util.UUID;

public record OrderId(
    String value
) {
    public static OrderId next() {
        return new OrderId(UUID.randomUUID().toString());
    }
}
