package ru.otus.hw.order.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import ru.otus.hw.core.Channels;
import ru.otus.hw.order.domain.OrderId;
import ru.otus.hw.order.domain.OrderStatus;
import ru.otus.hw.warehouse.application.OutOfStockEvent;

@Component
@RequiredArgsConstructor
public class CancelWhenOutOfStockHandler {

    private final OrderRepository orderRepository;

    @Qualifier(Channels.ORDER_CANCELED_EVENT)
    private final MessageChannel orderCanceledEventChannel;

    public void handle(OutOfStockEvent event) {
        var order = orderRepository.findById(event.orderId())
            .orElseThrow(() -> new OrderNotFoundException(event.orderId()));

        var updatedOrder = order.toBuilder().status(OrderStatus.CANCELED).build();

        orderRepository.save(updatedOrder);

        sendOrderCanceledEvent(event.orderId());
    }

    private void sendOrderCanceledEvent(OrderId orderId) {
        var reason = "Some products are out of stock";
        var orderCanceledEvent = new OrderCanceledEvent(orderId, reason);
        var orderCanceledMessage = MessageBuilder.withPayload(orderCanceledEvent).build();
        orderCanceledEventChannel.send(orderCanceledMessage);
    }
}
