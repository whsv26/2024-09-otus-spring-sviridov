package ru.otus.hw.warehouse;

import ru.otus.hw.core.CrudRepository;
import ru.otus.hw.marketing.ProductId;

public interface InventoryItemRepository extends CrudRepository<ProductId, InventoryItem> {
}
