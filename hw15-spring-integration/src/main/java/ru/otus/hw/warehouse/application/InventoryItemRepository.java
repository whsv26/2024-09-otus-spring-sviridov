package ru.otus.hw.warehouse.application;

import ru.otus.hw.core.CrudRepository;
import ru.otus.hw.marketing.domain.ProductId;
import ru.otus.hw.warehouse.domain.InventoryItem;

public interface InventoryItemRepository extends CrudRepository<ProductId, InventoryItem> {
}
