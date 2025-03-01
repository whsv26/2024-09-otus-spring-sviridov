package ru.otus.hw.order.application;

import ru.otus.hw.core.CrudRepository;
import ru.otus.hw.order.domain.Order;
import ru.otus.hw.order.domain.OrderId;

public interface OrderRepository extends CrudRepository<OrderId, Order> {
}
