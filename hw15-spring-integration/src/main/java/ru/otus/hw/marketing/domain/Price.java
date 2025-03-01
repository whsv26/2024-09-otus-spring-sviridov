package ru.otus.hw.marketing.domain;

import java.math.BigDecimal;
import java.util.Currency;

public record Price(
    BigDecimal amount,
    Currency currency
) { }
