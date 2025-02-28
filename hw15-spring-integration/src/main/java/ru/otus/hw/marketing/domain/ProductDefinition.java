package ru.otus.hw.marketing.domain;

import java.util.List;

public record ProductDefinition(
    String id,
    String title,
    String description,
    Price basePrice,
    List<PricingPolicy> pricingPolicies
) { }
