package ru.otus.hw.order.presentation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.hw.marketing.domain.ProductId;
import ru.otus.hw.order.domain.Order;
import ru.otus.hw.order.domain.OrderLineItem;
import ru.otus.hw.order.application.OrderPlacedEvent;
import ru.otus.hw.order.application.PlaceOrderCommand;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "id", expression = "java(OrderId.next())")
    @Mapping(target = "status", constant = "ACTIVE")
    Order toOrder(PlaceOrderCommand command);

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "quantityByProduct", source = "items")
    OrderPlacedEvent toEvent(Order order);

    default Map<ProductId, Integer> map(List<OrderLineItem> items) {
        return items.stream().collect(
            Collectors.toMap(
                OrderLineItem::productId,
                OrderLineItem::quantity
            )
        );
    }
}
