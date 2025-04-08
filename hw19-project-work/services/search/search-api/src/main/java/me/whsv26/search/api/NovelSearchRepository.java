package me.whsv26.search.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NovelSearchRepository {

    Page<Novel> search(NovelSearchParams params, Pageable pageable);
}
