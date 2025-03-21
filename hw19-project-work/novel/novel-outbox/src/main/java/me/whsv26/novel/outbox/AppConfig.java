package me.whsv26.novel.outbox;

import me.whsv26.libs.outbox.mongo.OutboxChangeStreamWatcher;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.DefaultSslBundleRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class AppConfig {

    @Bean
    public ProducerFactory<String, String> producerFactory(KafkaProperties kafkaProperties) {
        var props = kafkaProperties.buildProducerProperties(new DefaultSslBundleRegistry());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);

        var kafkaProducerFactory = new DefaultKafkaProducerFactory<String, String>(props);
        kafkaProducerFactory.setValueSerializer(new StringSerializer());
        return kafkaProducerFactory;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(
        ProducerFactory<String, String> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    OutboxChangeStreamWatcher outboxChangeStreamWatcher(
        KafkaTemplate<String, String> kafkaTemplate,
        MongoTemplate mongoTemplate
    ) {
        return new OutboxChangeStreamWatcher(kafkaTemplate, mongoTemplate);
    }
}
