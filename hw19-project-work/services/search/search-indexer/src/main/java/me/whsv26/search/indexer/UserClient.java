package me.whsv26.search.indexer;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Component
public class UserClient {

    private final RestClient client;

    public UserClient(
        RestClient.Builder clientBuilder,
        RestProps props
    ) {
        this.client = clientBuilder
            .baseUrl(props.userApi().url().toString())
            .build();
    }

    public Optional<String> getUsername(String userId) {
        var maybeBody = client.get()
            .uri("/internal/users/{userId}", userId)
            .retrieve()
            .body(ReadUserResponse.class);

        return Optional.ofNullable(maybeBody)
            .map(body -> body.user.username)
            .filter(username -> !username.isEmpty());
    }

    private record ReadUserResponse(UserResponse user) {}

    private record UserResponse(String username) {}
}