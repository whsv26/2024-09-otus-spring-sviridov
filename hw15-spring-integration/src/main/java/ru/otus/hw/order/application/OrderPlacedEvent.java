package ru.otus.hw.order.application;

import ru.otus.hw.marketing.domain.ProductId;
import ru.otus.hw.order.domain.OrderId;

import java.util.Map;

public record OrderPlacedEvent(
    OrderId orderId,
    Map<ProductId, Integer> quantityByProduct
) { }
