package ru.otus.hw.order;

import ru.otus.hw.marketing.ProductId;

import java.math.BigDecimal;

public record OrderLineItem(
    ProductId productId,
    String title,
    BigDecimal price,
    int quantity
) {
    public BigDecimal getTotalPrice() {
        return price().multiply(BigDecimal.valueOf(quantity()));
    }
}
