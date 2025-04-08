package me.whsv26.novel.api.application.service;

import me.whsv26.libs.auth.CurrentUser;
import me.whsv26.libs.auth.CurrentUserProvider;
import me.whsv26.novel.api.application.dto.ChapterPreview;
import me.whsv26.novel.api.application.dto.PagedResult;
import me.whsv26.novel.api.application.port.out.ChapterRepository;
import me.whsv26.novel.api.application.port.out.NovelRepository;
import me.whsv26.novel.api.domain.entity.Chapter;
import me.whsv26.novel.api.domain.entity.Novel;
import me.whsv26.novel.api.domain.exception.ChapterNotFoundException;
import me.whsv26.novel.api.domain.exception.NovelNotFoundException;
import me.whsv26.novel.api.domain.valueobject.ChapterId;
import me.whsv26.novel.api.domain.valueobject.CreateNovelDetails;
import me.whsv26.novel.api.domain.valueobject.GenreId;
import me.whsv26.novel.api.domain.valueobject.NovelId;
import me.whsv26.novel.api.domain.valueobject.AuthorId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChapterServiceTest {

    @Mock
    private NovelRepository novelRepository;

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @Mock
    private Clock clock;

    @InjectMocks
    private ChapterService chapterService;

    private NovelId novelId;

    private ChapterId chapterId;

    private Novel novel;

    private Chapter chapter;

    private CurrentUser currentUser;

    @BeforeEach
    void setUp() {
        var now = Instant.parse("2025-01-01T00:00:00Z");
        when(clock.instant()).thenReturn(now);
        when(clock.getZone()).thenReturn(ZoneOffset.UTC);

        novelId = new NovelId("novel-123");
        chapterId = new ChapterId("chapter-123");
        currentUser = new CurrentUser("user-1");

        var details = new CreateNovelDetails(
            novelId,
            "Test Novel",
            "Test Novel Description",
            new AuthorId(currentUser.userId()),
            List.of(new GenreId("genre-1")),
            List.of("tag-1", "tag-2")
        );
        novel = Novel.create(details, clock);

        chapter = new Chapter(
            chapterId,
            novelId,
            "Original Title",
            "Original Content",
            clock
        );
    }

    @Test
    @DisplayName("Должен вернуть список глав по ID новеллы")
    void findByNovelId_ShouldReturnPagedResult() {
        int pageNumber = 1;
        int pageSize = 10;
        var preview = new ChapterPreview(chapterId, "Chapter Title");
        var pagedResult = new PagedResult<>(
            List.of(preview),
            pageNumber,
            pageSize,
            1,
            1,
            false
        );

        when(chapterRepository.findByNovelId(novelId, pageNumber, pageSize)).thenReturn(pagedResult);

        var result = chapterService.findByNovelId(novelId, pageNumber, pageSize);

        assertThat(result)
            .isNotNull()
            .usingRecursiveComparison()
            .isEqualTo(pagedResult);
    }

    @Test
    @DisplayName("Должен вернуть главу по ID")
    void findById_ShouldReturnChapter() {
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));
        var result = chapterService.findById(chapterId);
        assertThat(result)
            .isNotNull()
            .usingRecursiveComparison()
            .isEqualTo(chapter);
    }

    @Test
    @DisplayName("Должен выбросить исключение, если глава не найдена")
    void findById_ShouldThrowChapterNotFoundException() {
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.empty());
        assertThrows(ChapterNotFoundException.class, () -> chapterService.findById(chapterId));
    }

    @Test
    @DisplayName("Должен создать главу, если пользователь — автор")
    void create_ShouldSaveChapter_WhenUserIsAuthor() {
        when(novelRepository.findById(novelId)).thenReturn(Optional.of(novel));
        when(currentUserProvider.getCurrentUser()).thenReturn(currentUser);
        when(chapterRepository.save(any(Chapter.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        var result = chapterService.create(novelId, chapter.getTitle(), chapter.getContent());

        assertThat(result)
            .isNotNull()
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(chapter);
    }

    @Test
    @DisplayName("Должен выбросить исключение при создании главы, если пользователь не автор")
    void create_ShouldThrowAccessDeniedException_WhenUserIsNotAuthor() {
        var notAnAuthor = new CurrentUser("not-an-author");
        when(novelRepository.findById(novelId)).thenReturn(Optional.of(novel));
        when(currentUserProvider.getCurrentUser()).thenReturn(notAnAuthor);

        assertThrows(
            AccessDeniedException.class,
            () -> chapterService.create(novelId, "New Chapter", "New Content")
        );
    }

    @Test
    @DisplayName("Должен изменить главу, если пользователь — автор")
    void update_ShouldModifyChapter_WhenUserIsAuthor() {
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));
        when(novelRepository.findById(novel.getId())).thenReturn(Optional.of(novel));
        when(currentUserProvider.getCurrentUser()).thenReturn(currentUser);

        var updatedTitle = "Updated Title";
        var updatedContent = "Updated Content";
        var updatedChapter = chapter.toBuilder()
            .title(updatedTitle)
            .content(updatedContent)
            .build();

        when(chapterRepository.save(any(Chapter.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        var result = chapterService.update(chapterId, updatedTitle, updatedContent);

        assertThat(result)
            .isNotNull()
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(updatedChapter);
    }

    @Test
    @DisplayName("Должен выбросить исключение при обновлении главы, если пользователь не автор")
    void update_ShouldThrowAccessDeniedException_WhenUserIsNotAuthor() {
        var notAnAuthor = new CurrentUser("not-an-author");

        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));
        when(novelRepository.findById(novel.getId())).thenReturn(Optional.of(novel));
        when(currentUserProvider.getCurrentUser()).thenReturn(notAnAuthor);

        assertThrows(
            AccessDeniedException.class,
            () -> chapterService.update(chapterId, "Updated Title", "Updated Content")
        );
    }

    @Test
    @DisplayName("Должен удалить главу, если пользователь — автор")
    void delete_ShouldRemoveChapter_WhenUserIsAuthor() {
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));
        when(novelRepository.findById(novel.getId())).thenReturn(Optional.of(novel));
        when(currentUserProvider.getCurrentUser()).thenReturn(currentUser);

        assertDoesNotThrow(() -> chapterService.delete(chapterId));
    }

    @Test
    @DisplayName("Должен выбросить исключение при удалении главы, если пользователь не автор")
    void delete_ShouldThrowAccessDeniedException_WhenUserIsNotAuthor() {
        var notAnAuthor = new CurrentUser("not-an-author");
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));
        when(novelRepository.findById(novel.getId())).thenReturn(Optional.of(novel));
        when(currentUserProvider.getCurrentUser()).thenReturn(notAnAuthor);

        assertThrows(AccessDeniedException.class, () -> chapterService.delete(chapterId));
    }

    @Test
    @DisplayName("Должен удалить все главы новеллы, если пользователь — автор")
    void deleteByNovelId_ShouldRemoveAllChapters_WhenUserIsAuthor() {
        when(novelRepository.findById(novelId)).thenReturn(Optional.of(novel));
        when(currentUserProvider.getCurrentUser()).thenReturn(currentUser);
        when(chapterRepository.findAllByNovelId(novelId))
            .thenReturn(List.of(new ChapterPreview(chapterId, chapter.getTitle())));

        assertDoesNotThrow(() -> chapterService.deleteByNovelId(novelId));
    }

    @Test
    @DisplayName("Должен выбросить исключение при удалении всех глав, если пользователь не автор")
    void deleteByNovelId_ShouldThrowAccessDeniedException_WhenUserIsNotAuthor() {
        var notAnAuthor = new CurrentUser("not-an-author");
        when(novelRepository.findById(novelId)).thenReturn(Optional.of(novel));
        when(currentUserProvider.getCurrentUser()).thenReturn(notAnAuthor);

        assertThrows(AccessDeniedException.class, () -> chapterService.deleteByNovelId(novelId));
    }

    @Test
    @DisplayName("Должен выбросить исключение при создании главы, если новелла не найдена")
    void create_ShouldThrowNovelNotFoundException_WhenNovelDoesNotExist() {
        when(novelRepository.findById(novelId)).thenReturn(Optional.empty());

        assertThrows(
            NovelNotFoundException.class,
            () -> chapterService.create(novelId, "Title", "Content")
        );
    }

    @Test
    @DisplayName("Должен выбросить исключение при обновлении главы, если новелла не найдена")
    void update_ShouldThrowNovelNotFoundException_WhenNovelDoesNotExist() {
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));
        when(novelRepository.findById(novel.getId())).thenReturn(Optional.empty());

        assertThrows(
            NovelNotFoundException.class,
            () -> chapterService.update(chapterId, "New Title", "New Content")
        );
    }

    @Test
    @DisplayName("Должен выбросить исключение при удалении главы, если новелла не найдена")
    void delete_ShouldThrowNovelNotFoundException_WhenNovelDoesNotExist() {
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));
        when(novelRepository.findById(novel.getId())).thenReturn(Optional.empty());

        assertThrows(NovelNotFoundException.class, () -> chapterService.delete(chapterId));
    }
}
