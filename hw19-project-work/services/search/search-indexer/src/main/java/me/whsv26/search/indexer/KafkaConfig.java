package me.whsv26.search.indexer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
    public ConsumerFactory<Object, Object> generalConsumerFactory(
        KafkaProperties kafkaProperties,
        ObjectMapper mapper
    ) {
        var props = kafkaProperties.buildConsumerProperties(new DefaultSslBundleRegistry());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "me.whsv26.*");

        var kafkaConsumerFactory = new DefaultKafkaConsumerFactory<>(props);
        kafkaConsumerFactory.setValueDeserializer(new JsonDeserializer<>(mapper));
        return kafkaConsumerFactory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<?, ?> generalKafkaListenerContainerFactory(
        ConsumerFactory<Object, Object> consumerFactory,
        CommonErrorHandler errorHandler
    ) {
        var listenerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        listenerFactory.setConsumerFactory(consumerFactory);
        listenerFactory.setBatchListener(true);
        listenerFactory.setCommonErrorHandler(errorHandler);

        var containerProperties = listenerFactory.getContainerProperties();
        containerProperties.setAckMode(ContainerProperties.AckMode.BATCH);
        containerProperties.setObservationEnabled(true);

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
}
