package ru.otus.hw.order;

import java.util.UUID;

public record OrderId(
    String value
) {
    static OrderId next() {
        return new OrderId(UUID.randomUUID().toString());
    }
}
