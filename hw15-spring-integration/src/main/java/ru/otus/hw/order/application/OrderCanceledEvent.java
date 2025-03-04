package ru.otus.hw.order.application;

import ru.otus.hw.order.domain.OrderId;

public record OrderCanceledEvent(
    OrderId orderId,
    String reason
) { }
