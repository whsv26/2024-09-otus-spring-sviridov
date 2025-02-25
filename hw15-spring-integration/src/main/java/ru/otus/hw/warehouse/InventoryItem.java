package ru.otus.hw.warehouse;

import lombok.Builder;
import lombok.Getter;
import ru.otus.hw.core.HasId;
import ru.otus.hw.marketing.ProductId;

@Builder(toBuilder = true)
public record InventoryItem(
    @Getter ProductId id,
    int quantity,
    int reservedQuantity
) implements HasId<ProductId> {

    public int getAvailableQuantity() {
        return quantity - reservedQuantity;
    }
}
