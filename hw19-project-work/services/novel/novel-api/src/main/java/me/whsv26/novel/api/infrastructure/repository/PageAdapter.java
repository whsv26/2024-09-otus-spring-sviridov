package me.whsv26.novel.api.infrastructure.repository;

import me.whsv26.novel.api.application.dto.PagedResult;
import org.springframework.data.domain.Page;

public class PageAdapter {

    public static <T> PagedResult<T> toPagedResult(Page<T> page) {
        return new PagedResult<>(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }
}
