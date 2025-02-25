package ru.otus.hw.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaceOrderCommandHandler {

    private final OrderRepository orderRepository;

    private final AbstractOrderMapper orderMapper;

    OrderPlacedEvent handle(PlaceOrderCommand command) {
        var orderToSave = orderMapper.toOrder(command);
        var savedOrder = orderRepository.save(orderToSave);
        return orderMapper.toEvent(savedOrder);
    }
}
