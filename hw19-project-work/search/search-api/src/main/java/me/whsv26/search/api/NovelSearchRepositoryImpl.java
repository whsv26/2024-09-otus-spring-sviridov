package me.whsv26.search.api;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.json.JsonData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NovelSearchRepositoryImpl implements NovelSearchRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public Page<Novel> search(
        String prompt,
        String authorName,
        Range<Integer> ratingRange,
        List<String> genres,
        List<String> tags,
        Pageable pageable
    ) {
        var query = buildQuery(prompt, authorName, ratingRange, genres, tags);
        var nativeQuery = new NativeQueryBuilder()
            .withQuery(query)
            .withPageable(pageable)
            .build();

        var searchHits = elasticsearchOperations.search(nativeQuery, Novel.class);
        var novels = searchHits.getSearchHits()
            .stream()
            .map(SearchHit::getContent)
            .toList();

        return new PageImpl<>(novels, pageable, searchHits.getTotalHits());
    }

    private static Query buildQuery(
        String prompt,
        String authorName,
        Range<Integer> ratingRange,
        List<String> genres,
        List<String> tags
    ) {
        var boolQuery = QueryBuilders.bool();

        if (prompt != null) {
            addTitleQuery(prompt, boolQuery);
        }

        if (authorName != null) {
            addAuthorNameQuery(authorName, boolQuery);
        }

        if (genres != null && !genres.isEmpty()) {
            addGenresQuery(genres, boolQuery);
        }

        if (tags != null && !tags.isEmpty()) {
            addTagsQuery(tags, boolQuery);
        }

        if (ratingRange != null) {
            addRatingQuery(ratingRange, boolQuery);
        }

        return boolQuery.build()._toQuery();
    }

    private static void addRatingQuery(Range<Integer> ratingRange, BoolQuery.Builder boolQuery) {
        boolQuery.filter(query ->
            query.range(rangeQuery -> {
                ratingRange.getFrom().ifPresent(ratingFrom ->
                    rangeQuery.gte(JsonData.of(ratingFrom))
                );
                ratingRange.getTo().ifPresent(ratingTo ->
                    rangeQuery.lte(JsonData.of(ratingTo))
                );
                return rangeQuery;
            })
        );
    }

    private static void addTagsQuery(List<String> tags, BoolQuery.Builder boolQuery) {
        boolQuery.filter(query ->
            query.terms(termsQuery ->
                termsQuery.field("tags").terms(termsQueryField ->
                    termsQueryField.value(tags.stream().map(FieldValue::of).toList())
                )
            )
        );
    }

    private static void addGenresQuery(List<String> genres, BoolQuery.Builder boolQuery) {
        boolQuery.filter(query ->
            query.terms(termsQuery ->
                termsQuery.field("genres").terms(termsQueryField ->
                    termsQueryField.value(genres.stream().map(FieldValue::of).toList())
                )
            )
        );
    }

    private static void addAuthorNameQuery(String authorName, BoolQuery.Builder boolQuery) {
        boolQuery.must(query ->
            query.match(matchQuery ->
                matchQuery.query(authorName).field("authorName")
            )
        );
    }

    private static void addTitleQuery(String prompt, BoolQuery.Builder boolQuery) {
        boolQuery.must(query ->
            query.multiMatch(multiMatchQuery ->
                multiMatchQuery.query(prompt).fields("title", "synopsis")
            )
        );
    }
}
