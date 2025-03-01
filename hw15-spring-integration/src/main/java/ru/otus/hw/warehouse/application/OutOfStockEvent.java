package ru.otus.hw.warehouse.application;

import ru.otus.hw.marketing.domain.ProductId;
import ru.otus.hw.order.domain.OrderId;

public record OutOfStockEvent(
    OrderId orderId,
    ProductId productId
) {
}
