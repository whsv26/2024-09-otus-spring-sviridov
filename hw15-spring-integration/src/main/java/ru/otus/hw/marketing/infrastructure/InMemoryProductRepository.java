package ru.otus.hw.marketing.infrastructure;

import org.springframework.stereotype.Repository;
import ru.otus.hw.core.AbstractInMemoryRepository;
import ru.otus.hw.marketing.application.ProductRepository;
import ru.otus.hw.marketing.domain.Product;
import ru.otus.hw.marketing.domain.ProductId;

@Repository
public class InMemoryProductRepository
    extends AbstractInMemoryRepository<ProductId, Product>
    implements ProductRepository { }
