package ru.otus.hw.marketing.domain;

import java.util.Optional;

public record PricingConditions(
    Optional<String> area,
    Optional<Integer> minQuantity,
    Optional<Integer> maxQuantity,
    Optional<String> promoCode
) { }
