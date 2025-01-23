package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.User;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с пользователями ")
@DataMongoTest
@Import(MongockConfig.class)
public class MongoUserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate template;

    @DisplayName("должен находить пользователя по его логину")
    @Test
    void shouldFindUserByUsername() {
        var userId = "1";
        var username = "admin";
        var expectedUser = template.findById(userId, User.class);
        var actualUser = userRepository.findByUsername(username);

        assertThat(actualUser)
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(expectedUser);
    }
}
