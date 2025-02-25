package ru.otus.hw.order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.marketing.ProductId;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderGateway orderGateway;

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders/{orderId}")
    public ViewOrderResponse viewOrder(@PathVariable("orderId") String id) {
        var orderId = new OrderId(id);
        var order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException(orderId));

        return Mappers.getMapper(OrderControllerMapper.class).map(order);
    }

    @PostMapping("/api/v1/orders")
    public CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
        var command = Mappers.getMapper(OrderControllerMapper.class).map(request);
        var event = orderGateway.dispatch(command);
        return new CreateOrderResponse(event.orderId().value());
    }

    public record CreateOrderRequest(
        List<OrderLineItem> items,
        Currency currency,
        String email
    ) { }

    public record CreateOrderResponse(
        String orderId
    ) { }

    public record ViewOrderResponse(
        String id,
        List<OrderLineItem> items,
        Currency currency,
        String email,
        String status
    ) { }

    record OrderLineItem(
        String productId,
        String title,
        BigDecimal price,
        int quantity
    ) { }

    @Mapper
    interface OrderControllerMapper {

        PlaceOrderCommand map(CreateOrderRequest request);

        ViewOrderResponse map(Order order);

        default ProductId map(String source) {
            return new ProductId(source);
        }

        default String map(ProductId source) {
            return source.value();
        }

        default String map(OrderId source) {
            return source.value();
        }
    }
}
