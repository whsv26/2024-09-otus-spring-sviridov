package ru.otus.hw.marketing.domain;

import lombok.Getter;
import ru.otus.hw.core.HasId;

import java.util.Optional;

public record Product(
    @Getter ProductId id,
    String title,
    String description,
    Price price,
    String area,
    Integer minQuantity,
    Optional<Integer> maxQuantity,
    Optional<String> promoCode
) implements HasId<ProductId> { }
