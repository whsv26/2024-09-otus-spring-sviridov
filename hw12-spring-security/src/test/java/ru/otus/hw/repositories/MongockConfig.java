package ru.otus.hw.repositories;

import io.mongock.driver.mongodb.springdata.v4.config.SpringDataMongoV4Context;
import io.mongock.runner.springboot.EnableMongock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableMongock
@Import({SpringDataMongoV4Context.class})
public class MongockConfig {

    @Bean
    @Primary
    public PasswordEncoder passwordEncoderTest() {
        return NoOpPasswordEncoder.getInstance();
    }
}
