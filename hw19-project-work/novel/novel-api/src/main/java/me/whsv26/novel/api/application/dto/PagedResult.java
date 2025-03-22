package me.whsv26.novel.api.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public final class PagedResult<T> {

    private List<T> elements;

    private int pageNumber;

    private int pageSize;

    private long totalElements;

    private int totalPages;
}
