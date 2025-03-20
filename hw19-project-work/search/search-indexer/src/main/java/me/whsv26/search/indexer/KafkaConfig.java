package me.whsv26.search.indexer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.whsv26.novel.model.NovelEvent;
import me.whsv26.rating.model.NovelRatingEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.DefaultSslBundleRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@Slf4j
public class KafkaConfig {

    @Bean
    public ConsumerFactory<String, NovelEvent> consumerFactoryNovelEvent(
        KafkaProperties kafkaProperties,
        ObjectMapper mapper
    ) {
        return buildConsumerFactoryFor(kafkaProperties, mapper);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NovelEvent> kafkaListenerContainerFactoryNovelEvent(
        ConsumerFactory<String, NovelEvent> consumerFactory,
        CommonErrorHandler errorHandler
    ) {
        var listenerFactory = new ConcurrentKafkaListenerContainerFactory<String, NovelEvent>();
        listenerFactory.setConsumerFactory(consumerFactory);
        listenerFactory.setBatchListener(true);
        listenerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);
        listenerFactory.setCommonErrorHandler(errorHandler);

        return listenerFactory;
    }

    @Bean
    public ConsumerFactory<String, NovelRatingEvent> consumerFactoryNovelRatingEvent(
        KafkaProperties kafkaProperties,
        ObjectMapper mapper
    ) {
        return buildConsumerFactoryFor(kafkaProperties, mapper);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NovelRatingEvent> kafkaListenerContainerFactoryNovelRatingEvent(
        ConsumerFactory<String, NovelRatingEvent> consumerFactory,
        CommonErrorHandler errorHandler
    ) {
        var listenerFactory = new ConcurrentKafkaListenerContainerFactory<String, NovelRatingEvent>();
        listenerFactory.setConsumerFactory(consumerFactory);
        listenerFactory.setBatchListener(true);
        listenerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);
        listenerFactory.setCommonErrorHandler(errorHandler);

        return listenerFactory;
    }

    @Bean
    public CommonErrorHandler commonErrorHandler() {
        var fixedBackOff = new FixedBackOff(5000, 3);
        var errorHandler = new DefaultErrorHandler(
            (record, exception) -> log.error(
                "Error processing record with value: %s".formatted(record.value()),
                exception
            ),
            fixedBackOff
        );
        errorHandler.addNotRetryableExceptions(NullPointerException.class);
        return errorHandler;
    }

    private static <T> ConsumerFactory<String, T> buildConsumerFactoryFor(
        KafkaProperties kafkaProperties,
        ObjectMapper mapper
    ) {
        var props = kafkaProperties.buildConsumerProperties(new DefaultSslBundleRegistry());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "me.whsv26");
//        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

        var kafkaConsumerFactory = new DefaultKafkaConsumerFactory<String, T>(props);
        kafkaConsumerFactory.setValueDeserializer(new JsonDeserializer<>(mapper));
        return kafkaConsumerFactory;
    }
}
