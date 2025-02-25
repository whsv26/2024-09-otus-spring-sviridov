package ru.otus.hw.order;

import org.springframework.stereotype.Repository;
import ru.otus.hw.core.AbstractInMemoryRepository;

@Repository
public class InMemoryOrderRepository
    extends AbstractInMemoryRepository<OrderId, Order>
    implements OrderRepository {
}
