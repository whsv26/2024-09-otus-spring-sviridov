package ru.otus.hw.warehouse;

import ru.otus.hw.marketing.ProductId;

public class InventoryItemNotFoundException extends RuntimeException {
    public InventoryItemNotFoundException(ProductId productId) {
        super("Inventory item not found: " + productId);
    }
}
