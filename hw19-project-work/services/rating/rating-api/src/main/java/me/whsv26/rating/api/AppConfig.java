package me.whsv26.rating.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.whsv26.libs.auth.CurrentUserProvider;
import me.whsv26.libs.auth.PreAuthenticationFilter;
import me.whsv26.libs.auth.SecurityContextHolderCurrentUserProvider;
import me.whsv26.rating.model.NovelRatingCommand;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.DefaultSslBundleRegistry;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class AppConfig {

    @Bean
    public ProducerFactory<String, NovelRatingCommand> producerFactory(
        KafkaProperties kafkaProperties,
        ObjectMapper mapper
    ) {
        var props = kafkaProperties.buildProducerProperties(new DefaultSslBundleRegistry());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        var kafkaProducerFactory = new DefaultKafkaProducerFactory<String, NovelRatingCommand>(props);
        kafkaProducerFactory.setValueSerializer(new JsonSerializer<>(mapper));
        return kafkaProducerFactory;
    }

    @Bean
    public KafkaTemplate<String, NovelRatingCommand> kafkaTemplate(
        ProducerFactory<String, NovelRatingCommand> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    CurrentUserProvider currentUserProvider() {
        return new SecurityContextHolderCurrentUserProvider();
    }

    @Bean
    FilterRegistrationBean<PreAuthenticationFilter> preAuthenticationFilterRegistration() {
        var filter = new PreAuthenticationFilter();
        var registration = new FilterRegistrationBean<PreAuthenticationFilter>();
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setOrder(0);
        return registration;
    }
}
