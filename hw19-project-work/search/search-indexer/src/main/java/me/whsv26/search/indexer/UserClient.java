package me.whsv26.search.indexer;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

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

    public String getUsername(String userId) {
        var body = client.get()
            .uri("/internal/users/{userId}", userId)
            .retrieve()
            .body(ReadUserResponse.class);

        return body != null ? body.user.username : ""; // TODO
    }

    private record ReadUserResponse(UserResponse user) {}

    private record UserResponse(String username) {}
}