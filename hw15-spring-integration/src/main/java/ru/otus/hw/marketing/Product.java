package ru.otus.hw.marketing;

import lombok.Getter;
import ru.otus.hw.core.HasId;
import ru.otus.hw.core.Price;

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
