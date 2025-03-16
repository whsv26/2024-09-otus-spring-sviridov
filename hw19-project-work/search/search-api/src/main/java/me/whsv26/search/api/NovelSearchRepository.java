package me.whsv26.search.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NovelSearchRepository {
    Page<Novel> search(
        String maybePrompt,
        String maybeAuthorName,
        Range<Integer> maybeRatingRange,
        List<String> genres,
        List<String> tags,
        Pageable pageable
    );
}
