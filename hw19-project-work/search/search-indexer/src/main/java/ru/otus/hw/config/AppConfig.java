package ru.otus.hw.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.DefaultSslBundleRegistry;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import static org.springframework.kafka.support.serializer.JsonDeserializer.TYPE_MAPPINGS;

@Configuration
public class AppConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return JacksonUtils.enhancedObjectMapper();
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory(
        KafkaProperties kafkaProperties, ObjectMapper mapper) {
        var props = kafkaProperties.buildConsumerProperties(new DefaultSslBundleRegistry());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 3);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 3_000);

        var kafkaConsumerFactory = new DefaultKafkaConsumerFactory<String, String>(props);
        kafkaConsumerFactory.setValueDeserializer(new JsonDeserializer<>(mapper));
        return kafkaConsumerFactory;
    }
}
