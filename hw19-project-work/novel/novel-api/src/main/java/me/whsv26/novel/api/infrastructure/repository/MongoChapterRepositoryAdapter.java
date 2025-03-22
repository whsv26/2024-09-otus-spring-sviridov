package me.whsv26.novel.api.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import me.whsv26.novel.api.application.dto.ChapterPreview;
import me.whsv26.novel.api.application.dto.PagedResult;
import me.whsv26.novel.api.application.port.out.ChapterRepository;
import me.whsv26.novel.api.domain.entity.Chapter;
import me.whsv26.novel.api.domain.valueobject.ChapterId;
import me.whsv26.novel.api.domain.valueobject.NovelId;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MongoChapterRepositoryAdapter implements ChapterRepository {

    private final MongoChapterRepository underlying;

    @Override
    public PagedResult<ChapterPreview> findByNovelId(NovelId novelId, int page, int size) {
        var paged = underlying.findByNovelId(novelId, PageRequest.of(page, size));
        return PageAdapter.toPagedResult(paged);
    }

    @Override
    public List<ChapterPreview> findAllByNovelId(NovelId novelId) {
        return underlying.findAllByNovelId(novelId);
    }

    @Override
    public Optional<Chapter> findById(ChapterId id) {
        return underlying.findById(id);
    }

    @Override
    public Chapter save(Chapter entity) {
        return underlying.save(entity);
    }

    @Override
    public void deleteById(ChapterId id) {
        underlying.deleteById(id);
    }

    @Override
    public void deleteAllById(List<ChapterId> ids) {
        underlying.deleteAllById(ids);
    }

}
