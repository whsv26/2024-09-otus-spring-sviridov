package ru.otus.hw.order;

import java.util.Currency;
import java.util.List;

public record PlaceOrderCommand(
    List<OrderLineItem> items,
    Currency currency,
    String email
) { }
