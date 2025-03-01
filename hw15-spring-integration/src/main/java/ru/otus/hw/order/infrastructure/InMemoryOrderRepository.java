package ru.otus.hw.order.infrastructure;

import org.springframework.stereotype.Repository;
import ru.otus.hw.core.AbstractInMemoryRepository;
import ru.otus.hw.order.domain.Order;
import ru.otus.hw.order.domain.OrderId;
import ru.otus.hw.order.application.OrderRepository;

@Repository
public class InMemoryOrderRepository
    extends AbstractInMemoryRepository<OrderId, Order>
    implements OrderRepository {
}
