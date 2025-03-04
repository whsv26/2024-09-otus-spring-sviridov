package ru.otus.hw.order.application;

import ru.otus.hw.order.domain.OrderId;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(OrderId orderId) {
        super("Order not found: " + orderId);
    }
}
