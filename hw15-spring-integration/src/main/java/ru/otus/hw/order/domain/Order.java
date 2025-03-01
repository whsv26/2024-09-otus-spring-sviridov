package ru.otus.hw.order.domain;

import lombok.Builder;
import lombok.Getter;
import ru.otus.hw.core.HasId;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

@Builder(toBuilder = true)
public record Order(
    @Getter OrderId id,
    List<OrderLineItem> items,
    Currency currency,
    OrderStatus status,
    String email
) implements HasId<OrderId> {

    public BigDecimal getSum() {
        return items.stream()
            .map(OrderLineItem::getTotalPrice)
            .reduce(BigDecimal.valueOf(0), BigDecimal::add);
    }
}
