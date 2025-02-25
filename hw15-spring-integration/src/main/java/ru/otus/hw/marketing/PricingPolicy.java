package ru.otus.hw.marketing;

import ru.otus.hw.core.Price;

public record PricingPolicy(
    String policyId,
    PriceType priceType,
    Price price,
    PricingConditions conditions
) { }
