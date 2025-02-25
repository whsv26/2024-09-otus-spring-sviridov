package ru.otus.hw.order;

public record OrderCanceledEvent(
    OrderId orderId,
    String reason
) { }
