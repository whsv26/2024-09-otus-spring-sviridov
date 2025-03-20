package me.whsv26.rating.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.whsv26.rating.model.NovelRatingCommand;
import me.whsv26.rating.model.NovelRatingEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.DefaultSslBundleRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@Slf4j
public class KafkaConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return JacksonUtils.enhancedObjectMapper();
    }

    @Bean
    public ConsumerFactory<String, NovelRatingCommand> consumerFactory(
        KafkaProperties kafkaProperties,
        ObjectMapper mapper
    ) {
        var props = kafkaProperties.buildConsumerProperties(new DefaultSslBundleRegistry());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        var kafkaConsumerFactory = new DefaultKafkaConsumerFactory<String, NovelRatingCommand>(props);
        kafkaConsumerFactory.setValueDeserializer(new JsonDeserializer<>(mapper));
        return kafkaConsumerFactory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NovelRatingCommand> kafkaListenerContainerFactory(
        ConsumerFactory<String, NovelRatingCommand> consumerFactory
    ) {
        var listenerFactory = new ConcurrentKafkaListenerContainerFactory<String, NovelRatingCommand>();
        listenerFactory.setConsumerFactory(consumerFactory);
        listenerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);

        var fixedBackOff = new FixedBackOff(5000, 3);
        var errorHandler = new DefaultErrorHandler(
            (record, exception) -> log.error(
                "Error processing record with value: %s".formatted(record.value()),
                exception
            ),
            fixedBackOff
        );
        errorHandler.addNotRetryableExceptions(NullPointerException.class);
        listenerFactory.setCommonErrorHandler(errorHandler);

        return listenerFactory;
    }

    @Bean
    public ProducerFactory<String, NovelRatingEvent> producerFactory(
        KafkaProperties kafkaProperties,
        ObjectMapper mapper
    ) {
        var props = kafkaProperties.buildProducerProperties(new DefaultSslBundleRegistry());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        var kafkaProducerFactory = new DefaultKafkaProducerFactory<String, NovelRatingEvent>(props);
        kafkaProducerFactory.setValueSerializer(new JsonSerializer<>(mapper));
        return kafkaProducerFactory;
    }

    @Bean
    public KafkaTemplate<String, NovelRatingEvent> kafkaTemplate(
        ProducerFactory<String, NovelRatingEvent> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }
}
