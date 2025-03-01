package ru.otus.hw.order.application;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.core.Channels;

@MessagingGateway
public interface OrderGateway {

    @Gateway(
        requestChannel = Channels.PLACE_ORDER_COMMAND,
        replyChannel = Channels.ORDER_PLACED_EVENT
    )
    OrderPlacedEvent dispatch(PlaceOrderCommand command);
}
