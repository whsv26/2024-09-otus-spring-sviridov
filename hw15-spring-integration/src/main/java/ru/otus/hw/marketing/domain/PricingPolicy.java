package ru.otus.hw.marketing.domain;

public record PricingPolicy(
    String policyId,
    PriceType priceType,
    Price price,
    PricingConditions conditions
) { }
