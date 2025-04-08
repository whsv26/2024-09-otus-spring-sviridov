package me.whsv26.novel.api.application.service;

import me.whsv26.libs.auth.CurrentUser;
import me.whsv26.libs.auth.CurrentUserProvider;
import me.whsv26.novel.api.domain.exception.NovelNotFoundException;
import me.whsv26.novel.api.domain.valueobject.AuthorId;
import me.whsv26.novel.api.domain.valueobject.CreateNovelDetails;
import me.whsv26.novel.api.domain.valueobject.GenreId;
import me.whsv26.novel.api.domain.entity.Novel;
import me.whsv26.novel.api.domain.valueobject.NovelId;
import me.whsv26.novel.api.infrastructure.repository.MongoNovelRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NovelServiceTest {

    @Mock
    private MongoNovelRepository novelRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @Mock
    private Clock clock;

    @InjectMocks
    private NovelService novelService;

    private NovelId novelId;

    private Novel novel;

    private CurrentUser currentUser;

    private AuthorId authorId;

    @BeforeEach
    void setUp() {
        var now = Instant.parse("2025-01-01T00:00:00Z");
        when(clock.instant()).thenReturn(now);
        when(clock.getZone()).thenReturn(ZoneOffset.UTC);

        novelId = new NovelId("novel-123");
        authorId = new AuthorId("user-1");
        currentUser = new CurrentUser("user-1");

        var createDetails = new CreateNovelDetails(
            novelId,
            "Test Novel",
            "Test Synopsis",
            authorId,
            List.of(new GenreId("genre-1")),
            List.of("tag-1", "tag-2")
        );
        novel = Novel.create(createDetails, clock);
    }

    @Test
    @DisplayName("Должен вернуть список новелл по ID автора")
    void findByAuthorId_ShouldReturnNovelsForAuthor() {
        var novels = List.of(novel);
        when(novelRepository.findByAuthorId(authorId)).thenReturn(novels);
        var result = novelService.findByAuthorId(authorId);
        assertThat(result)
            .isNotNull()
            .hasSize(1)
            .containsExactly(novel);
    }

    @Test
    @DisplayName("Должен вернуть новеллу по ID")
    void findById_ShouldReturnNovel() {
        when(novelRepository.findById(novelId)).thenReturn(Optional.of(novel));
        var result = novelService.findById(novelId);
        assertThat(result)
            .isNotNull()
            .isEqualTo(novel);
    }

    @Test
    @DisplayName("Должен выбросить исключение, если новелла не найдена")
    void findById_ShouldThrowNovelNotFoundException() {
        when(novelRepository.findById(novelId)).thenReturn(Optional.empty());
        assertThrows(NovelNotFoundException.class, () -> novelService.findById(novelId));
    }

    @Test
    @DisplayName("Должен создать новеллу")
    void create_ShouldSaveNovel() {
        when(novelRepository.save(any(Novel.class))).thenReturn(novel);

        var result = novelService.create(
            authorId,
            "Test Novel",
            "Test Synopsis",
            List.of(new GenreId("genre-1")),
            List.of("tag-1", "tag-2")
        );

        assertThat(result)
            .isNotNull()
            .usingRecursiveComparison()
            .isEqualTo(novel);
    }

    @Test
    @DisplayName("Должен изменить новеллу, если пользователь — автор")
    void update_ShouldModifyNovel_WhenUserIsAuthor() {
        when(novelRepository.findById(novelId)).thenReturn(Optional.of(novel));
        when(currentUserProvider.getCurrentUser()).thenReturn(currentUser);

        var updatedTitle = "Updated Title";
        var updatedSynopsis = "Updated Synopsis";

        when(novelRepository.save(any(Novel.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        var updatedGenres = Collections.<GenreId>emptyList();
        var updatedTags = List.of("tag-1");
        var result = novelService.update(novelId, updatedTitle, updatedSynopsis, updatedGenres, updatedTags);
        var updatedNovel = novel.toBuilder()
            .title(updatedTitle)
            .synopsis(updatedSynopsis)
            .genres(updatedGenres)
            .tags(updatedTags)
            .build();

        assertThat(result)
            .isNotNull()
            .usingRecursiveComparison()
            .ignoringFields("domainEvents")
            .isEqualTo(updatedNovel);
    }

    @Test
    @DisplayName("Должен выбросить исключение при обновлении новеллы, если пользователь не автор")
    void update_ShouldThrowAccessDeniedException_WhenUserIsNotAuthor() {
        var notAnAuthor = new CurrentUser("not-an-author");
        when(novelRepository.findById(novelId)).thenReturn(Optional.of(novel));
        when(currentUserProvider.getCurrentUser()).thenReturn(notAnAuthor);
        assertThrows(
            AccessDeniedException.class,
            () -> novelService.update(
                novelId,
                "Updated Title",
                "Updated Synopsis",
                List.of(new GenreId("genre-1")),
                List.of("tag-1")
            )
        );
    }

    @Test
    @DisplayName("Должен выбросить исключение при обновлении новеллы, если новелла не найдена")
    void update_ShouldThrowNovelNotFoundException_WhenNovelDoesNotExist() {
        when(novelRepository.findById(novelId)).thenReturn(Optional.empty());
        assertThrows(
            NovelNotFoundException.class,
            () -> novelService.update(
                novelId,
                "Updated Title",
                "Updated Synopsis",
                List.of(new GenreId("genre-1")),
                List.of("tag-1")
            )
        );
    }

    @Test
    @DisplayName("Должен удалить новеллу, если пользователь — автор")
    void delete_ShouldRemoveNovel_WhenUserIsAuthor() {
        when(novelRepository.findById(novelId)).thenReturn(Optional.of(novel));
        when(currentUserProvider.getCurrentUser()).thenReturn(currentUser);
        assertDoesNotThrow(() -> novelService.delete(novelId));
    }

    @Test
    @DisplayName("Должен выбросить исключение при удалении новеллы, если пользователь не автор")
    void delete_ShouldThrowAccessDeniedException_WhenUserIsNotAuthor() {
        var notAnAuthor = new CurrentUser("different-user");
        when(novelRepository.findById(novelId)).thenReturn(Optional.of(novel));
        when(currentUserProvider.getCurrentUser()).thenReturn(notAnAuthor);

        assertThrows(AccessDeniedException.class, () -> novelService.delete(novelId));
    }

    @Test
    @DisplayName("Должен выбросить исключение при удалении новеллы, если новелла не найдена")
    void delete_ShouldThrowNovelNotFoundException_WhenNovelDoesNotExist() {
        when(novelRepository.findById(novelId)).thenReturn(Optional.empty());
        assertThrows(NovelNotFoundException.class, () -> novelService.delete(novelId));
    }
}