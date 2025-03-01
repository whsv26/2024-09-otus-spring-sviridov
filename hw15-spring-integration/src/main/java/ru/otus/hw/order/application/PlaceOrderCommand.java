package ru.otus.hw.order.application;

import ru.otus.hw.order.domain.OrderLineItem;

import java.util.Currency;
import java.util.List;

public record PlaceOrderCommand(
    List<OrderLineItem> items,
    Currency currency,
    String email
) { }
