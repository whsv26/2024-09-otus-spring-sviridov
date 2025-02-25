package ru.otus.hw.warehouse;

import ru.otus.hw.marketing.ProductId;
import ru.otus.hw.order.OrderId;

public record OutOfStockEvent(
    OrderId orderId,
    ProductId productId
) {
}
