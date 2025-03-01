package ru.otus.hw.warehouse.application;

import ru.otus.hw.marketing.domain.ProductId;

public class InventoryItemNotFoundException extends RuntimeException {
    public InventoryItemNotFoundException(ProductId productId) {
        super("Inventory item not found: " + productId);
    }
}
