package ru.otus.hw.warehouse;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import ru.otus.hw.core.Channels;
import ru.otus.hw.marketing.ProductId;
import ru.otus.hw.order.OrderPlacedEvent;

@Component
@RequiredArgsConstructor
public class ReserveWhenOrderPlaced {

    private final InventoryItemRepository inventoryItemRepository;

    @Qualifier(Channels.OUT_OF_STOCK_EVENT)
    private final MessageChannel outOfStockEventChannel;

    void handle(OrderPlacedEvent event) {
        try {
            ensureAllProductsInStock(event);
        } catch (OutOfStockException err) {
            sendOutOfStockEvent(event, err.getProductId());
        }

        reserveProducts(event);
    }

    private void reserveProducts(OrderPlacedEvent event) {
        event.quantityByProduct()
            .forEach((productId, requestedQuantity) -> {
                var inventoryItem = inventoryItemRepository.findById(productId)
                    .orElseThrow(() -> new InventoryItemNotFoundException(productId));

                var updatedReservedQuantity = inventoryItem.reservedQuantity() + requestedQuantity;
                var updatedInventoryItem = inventoryItem.toBuilder()
                    .reservedQuantity(updatedReservedQuantity)
                    .build();

                inventoryItemRepository.save(updatedInventoryItem);
            });
    }

    private void ensureAllProductsInStock(OrderPlacedEvent event) {
        event.quantityByProduct()
            .forEach((productId, requestedQuantity) -> {
                var availableQuantity = inventoryItemRepository.findById(productId)
                    .map(InventoryItem::getAvailableQuantity)
                    .orElse(0);

                if (requestedQuantity > availableQuantity) {
                    throw new OutOfStockException(productId);
                }
            });
    }

    private void sendOutOfStockEvent(OrderPlacedEvent event, ProductId productId) {
        var outOfStockEvent = new OutOfStockEvent(event.orderId(), productId);
        var outOfStockMessage = MessageBuilder.withPayload(outOfStockEvent).build();
        outOfStockEventChannel.send(outOfStockMessage);
    }
}
