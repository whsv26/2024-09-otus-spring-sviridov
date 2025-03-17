package me.whsv26.search.indexer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Component
public class RatingClient {

    private final RestClient client;

    public RatingClient(
        RestClient.Builder clientBuilder,
        @Value("${application.rest.rating-api.url}") String baseUrl
    ) {
        this.client = clientBuilder
            .baseUrl(baseUrl)
            .build();
    }

    public BigDecimal getNovelRating(String novelId) {
        var body = client.get()
            .uri("/internal/novels/{novelId}", novelId)
            .retrieve()
            .body(GetNovelRatingResponse.class);

        return body != null ? body.rating : BigDecimal.ZERO; // TODO
    }

    private record GetNovelRatingResponse(BigDecimal rating) {}
}