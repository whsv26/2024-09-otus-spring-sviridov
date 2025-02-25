package ru.otus.hw.marketing;

import ru.otus.hw.core.Price;

import java.util.List;

public record ProductDefinition(
    String id,
    String title,
    String description,
    Price basePrice,
    List<PricingPolicy> pricingPolicies
) { }
