package ru.otus.hw.warehouse.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.MessageHeaders;
import ru.otus.hw.core.Channels;
import ru.otus.hw.order.application.OrderPlacedEvent;
import ru.otus.hw.warehouse.application.ReserveWhenOrderPlacedHandler;

@Configuration
public class WarehouseIntegrationConfig {

    @Bean
    public IntegrationFlow reserveOrderProductsFlow(ReserveWhenOrderPlacedHandler handler) {
        return IntegrationFlow.from(Channels.ORDER_PLACED_EVENT)
            .handle((OrderPlacedEvent event, MessageHeaders headers) -> {
                handler.handle(event);
                return null;
            })
            .get();
    }
}