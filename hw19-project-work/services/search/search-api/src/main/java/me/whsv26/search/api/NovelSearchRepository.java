package me.whsv26.search.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import java.util.List;

public interface NovelSearchRepository {

    Page<Novel> search(
        @Nullable String prompt,
        @Nullable String authorName,
        @Nullable Range<Float> ratingRange,
        @Nullable List<String> genres,
        @Nullable List<String> tags,
        Pageable pageable
    );
}
