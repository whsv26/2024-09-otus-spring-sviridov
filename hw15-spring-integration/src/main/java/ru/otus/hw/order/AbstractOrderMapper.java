package ru.otus.hw.order;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.hw.marketing.ProductId;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class AbstractOrderMapper {

    @Mapping(target = "id", expression = "java(OrderId.next())")
    @Mapping(target = "status", expression = "java(OrderStatus.ACTIVE)")
    public abstract Order toOrder(PlaceOrderCommand command);

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "quantityByProduct", source = "items")
    public abstract OrderPlacedEvent toEvent(Order order);

    protected Map<ProductId, Integer> map(List<OrderLineItem> items) {
        return items.stream().collect(
            Collectors.toMap(
                OrderLineItem::productId,
                OrderLineItem::quantity
            )
        );
    }
}
