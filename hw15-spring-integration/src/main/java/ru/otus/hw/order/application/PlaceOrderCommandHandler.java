package ru.otus.hw.order.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.order.presentation.OrderMapper;

@Component
@RequiredArgsConstructor
public class PlaceOrderCommandHandler {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    public OrderPlacedEvent handle(PlaceOrderCommand command) {
        var orderToSave = orderMapper.toOrder(command);
        var savedOrder = orderRepository.save(orderToSave);
        return orderMapper.toEvent(savedOrder);
    }
}
