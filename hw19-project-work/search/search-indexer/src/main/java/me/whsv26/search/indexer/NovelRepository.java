package me.whsv26.search.indexer;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface NovelRepository extends ElasticsearchRepository<Novel, String> {
}
