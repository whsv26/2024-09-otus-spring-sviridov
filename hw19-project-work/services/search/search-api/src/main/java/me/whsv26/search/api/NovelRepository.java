package me.whsv26.search.api;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface NovelRepository extends ElasticsearchRepository<Novel, String>, NovelSearchRepository {
}
