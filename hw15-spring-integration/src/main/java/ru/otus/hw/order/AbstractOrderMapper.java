package ru.otus.hw.order;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class AbstractOrderMapper {

    @Mapping(target = "id", expression = "java(OrderId.next())")
    @Mapping(target = "status", expression = "java(OrderStatus.ACTIVE)")
    public abstract Order toOrder(PlaceOrderCommand command);

    public OrderPlacedEvent toEvent(Order order) {
        var quantityByProduct = order.items().stream().collect(
            Collectors.toMap(
                OrderLineItem::productId,
                OrderLineItem::quantity
            )
        );

        return new OrderPlacedEvent(order.id(), quantityByProduct);
    }
}
