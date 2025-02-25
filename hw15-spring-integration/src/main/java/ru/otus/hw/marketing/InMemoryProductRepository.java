package ru.otus.hw.marketing;

import org.springframework.stereotype.Repository;
import ru.otus.hw.core.AbstractInMemoryRepository;

@Repository
public class InMemoryProductRepository
    extends AbstractInMemoryRepository<ProductId, Product>
    implements ProductRepository { }
