package ru.otus.hw.warehouse;

import org.springframework.stereotype.Repository;
import ru.otus.hw.core.AbstractInMemoryRepository;
import ru.otus.hw.marketing.ProductId;

import java.util.List;

@Repository
public class InMemoryInventoryItemRepository
    extends AbstractInMemoryRepository<ProductId, InventoryItem>
    implements InventoryItemRepository {

    @Override
    protected List<InventoryItem> initializeWith() {
        return List.of(
            new InventoryItem(
                new ProductId("1"),
                10,
                5
            ),
            new InventoryItem(
                new ProductId("2"),
                1,
                0
            )
        );
    }
}
