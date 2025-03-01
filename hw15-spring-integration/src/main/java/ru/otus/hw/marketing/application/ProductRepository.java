package ru.otus.hw.marketing.application;

import ru.otus.hw.core.CrudRepository;
import ru.otus.hw.marketing.domain.Product;
import ru.otus.hw.marketing.domain.ProductId;

public interface ProductRepository extends CrudRepository<ProductId, Product> {
}
