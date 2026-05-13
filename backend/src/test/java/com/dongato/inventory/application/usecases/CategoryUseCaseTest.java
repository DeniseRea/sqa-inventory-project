package com.dongato.inventory.application.usecases;

import com.dongato.inventory.application.ports.out.CategoryRepositoryPort;
import com.dongato.inventory.domain.exception.BusinessRuleException;
import com.dongato.inventory.domain.exception.ResourceNotFoundException;
import com.dongato.inventory.domain.model.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Extended Unit tests for CategoryUseCase.
 * McCall Factor: Testability — isolated business logic tests.
 */
@ExtendWith(MockitoExtension.class)
class CategoryUseCaseTest {

    @Mock
    private CategoryRepositoryPort categoryRepository;

    @InjectMocks
    private CategoryUseCase categoryUseCase;

    // ─── create ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should create category successfully")
    void shouldCreateCategorySuccessfully() {
        Category input = Category.builder().name("CAFE").description("Cafés").build();
        Category saved = Category.builder().id(1L).name("CAFE").description("Cafés").build();

        when(categoryRepository.existsByName("CAFE")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(saved);

        Category result = categoryUseCase.create(input);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("CAFE", result.getName());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("Should throw BusinessRuleException when creating category with duplicate name")
    void shouldThrowOnDuplicateCategory() {
        Category input = Category.builder().name("CAFE").build();
        when(categoryRepository.existsByName("CAFE")).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> categoryUseCase.create(input));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when category name is blank on create")
    void shouldThrowWhenCategoryNameIsBlank() {
        Category input = Category.builder().name("").description("desc").build();

        assertThrows(IllegalArgumentException.class, () -> categoryUseCase.create(input));
        verify(categoryRepository, never()).existsByName(any());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when category name is null on create")
    void shouldThrowWhenCategoryNameIsNull() {
        Category input = Category.builder().name(null).build();

        assertThrows(IllegalArgumentException.class, () -> categoryUseCase.create(input));
    }

    // ─── findById ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should find category by ID successfully")
    void shouldFindById() {
        Category expected = Category.builder().id(1L).name("CAFE").build();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(expected));

        Category result = categoryUseCase.findById(1L);

        assertEquals("CAFE", result.getName());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when category not found by ID")
    void shouldThrowWhenNotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryUseCase.findById(99L));
    }

    // ─── findAll ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should return all categories")
    void shouldFindAll() {
        when(categoryRepository.findAll()).thenReturn(List.of(
                Category.builder().id(1L).name("CAFE").build(),
                Category.builder().id(2L).name("BEBIDA").build()
        ));

        List<Category> result = categoryUseCase.findAll();

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Should return empty list when no categories exist")
    void shouldReturnEmptyListWhenNoCategoriesExist() {
        when(categoryRepository.findAll()).thenReturn(List.of());

        List<Category> result = categoryUseCase.findAll();

        assertTrue(result.isEmpty());
    }

    // ─── update ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should update category name and description successfully")
    void shouldUpdateCategorySuccessfully() {
        Category existing = Category.builder().id(1L).name("CAFE").description("Old").build();
        Category updatePayload = Category.builder().name("CAFÉ PREMIUM").description("Updated").build();
        Category saved = Category.builder().id(1L).name("CAFÉ PREMIUM").description("Updated").build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(any(Category.class))).thenReturn(saved);

        Category result = categoryUseCase.update(1L, updatePayload);

        assertEquals("CAFÉ PREMIUM", result.getName());
        assertEquals("Updated", result.getDescription());
        verify(categoryRepository).save(existing);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existent category")
    void shouldThrowWhenUpdatingNonExistentCategory() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());
        Category payload = Category.builder().name("X").build();

        assertThrows(ResourceNotFoundException.class, () -> categoryUseCase.update(99L, payload));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when update has blank name")
    void shouldThrowWhenUpdateHasBlankName() {
        Category existing = Category.builder().id(1L).name("CAFE").build();
        Category updatePayload = Category.builder().name("   ").build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));

        assertThrows(IllegalArgumentException.class, () -> categoryUseCase.update(1L, updatePayload));
        verify(categoryRepository, never()).save(any());
    }

    // ─── delete ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should delete category successfully when it exists")
    void shouldDeleteSuccessfully() {
        when(categoryRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> categoryUseCase.delete(1L));
        verify(categoryRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent category")
    void shouldThrowWhenDeletingNonExistent() {
        when(categoryRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> categoryUseCase.delete(99L));
        verify(categoryRepository, never()).deleteById(any());
    }
}
