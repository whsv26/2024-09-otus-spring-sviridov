package ru.otus.hw.warehouse.application;

import lombok.Getter;
import ru.otus.hw.marketing.domain.ProductId;

@Getter
public class OutOfStockException extends RuntimeException {

    private final ProductId productId;

    public OutOfStockException(ProductId productId) {
        super("Inventory item is out of stock: " + productId);
        this.productId = productId;
    }
}
