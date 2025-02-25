package ru.otus.hw.order;

import ru.otus.hw.marketing.ProductId;

import java.util.Map;

public record OrderPlacedEvent(
    OrderId orderId,
    Map<ProductId, Integer> quantityByProduct
) { }
