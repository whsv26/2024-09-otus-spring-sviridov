package ru.otus.hw.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;

@Configuration
public class IntegrationConfig {

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2);
    }

    @Bean(Channels.PLACE_ORDER_COMMAND)
    public MessageChannel placeOrderCommandChannel() {
        return new QueueChannel(10);
    }

    @Bean(Channels.ORDER_PLACED_EVENT)
    public MessageChannel orderPlacedEventChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean(Channels.ORDER_CANCELED_EVENT)
    public MessageChannel orderCanceledEventChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean(Channels.OUT_OF_STOCK_EVENT)
    public MessageChannel outOfStockEventChannel() {
        return new PublishSubscribeChannel();
    }
}