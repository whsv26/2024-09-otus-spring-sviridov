package ru.otus.hw.order;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.mail.dsl.Mail;
import org.springframework.messaging.MessageHeaders;
import ru.otus.hw.core.Channels;
import ru.otus.hw.core.SmtpConfig;
import ru.otus.hw.warehouse.OutOfStockEvent;

@Configuration
public class OrderIntegrationConfig {

    @Bean
    IntegrationFlow placeOrderFlow(PlaceOrderCommandHandler handler) {
        return IntegrationFlow.from(Channels.PLACE_ORDER_COMMAND)
            .handle((PlaceOrderCommand command, MessageHeaders headers) -> handler.handle(command))
            .channel(Channels.ORDER_PLACED_EVENT)
            .get();
    }

    @Bean
    IntegrationFlow cancelOrderWhenOutOfStockFlow(CancelOrderWhenOutOfStockHandler handler) {
        return IntegrationFlow.from(Channels.OUT_OF_STOCK_EVENT)
            .handle((OutOfStockEvent event, MessageHeaders headers) -> {
                handler.handle(event);
                return null;
            })
            .get();
    }

    @Bean
    IntegrationFlow notifyWhenOrderCanceledFlow(NotifyWhenOrderCanceled handler, SmtpConfig config) {
        return IntegrationFlow.from(Channels.ORDER_CANCELED_EVENT)
            .transform(handler::handle)
            .handle(Mail.outboundAdapter(config.getHost())
                    .port(config.getPort())
                    .credentials(config.getUsername(), config.getPassword())
                    .javaMailProperties(p -> {
                        p.put("mail.debug", "true");
                        p.put("mail.smtp.ssl.trust", "*");
                        p.put("mail.smtp.starttls.enable", "true");
                    }))
            .get();
    }
}