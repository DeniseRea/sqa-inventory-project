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
 * Unit tests for CategoryUseCase.
 * McCall Factor: Testability — isolated business logic tests.
 */
@ExtendWith(MockitoExtension.class)
class CategoryUseCaseTest {

    @Mock
    private CategoryRepositoryPort categoryRepository;

    @InjectMocks
    private CategoryUseCase categoryUseCase;

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
    @DisplayName("Should throw when creating duplicate category")
    void shouldThrowOnDuplicateCategory() {
        Category input = Category.builder().name("CAFE").build();
        when(categoryRepository.existsByName("CAFE")).thenReturn(true);

        assertThrows(BusinessRuleException.class,
                () -> categoryUseCase.create(input));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should find category by ID")
    void shouldFindById() {
        Category expected = Category.builder().id(1L).name("CAFE").build();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(expected));

        Category result = categoryUseCase.findById(1L);

        assertEquals("CAFE", result.getName());
    }

    @Test
    @DisplayName("Should throw when category not found")
    void shouldThrowWhenNotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoryUseCase.findById(99L));
    }

    @Test
    @DisplayName("Should return all categories")
    void shouldFindAll() {
        when(categoryRepository.findAll()).thenReturn(
                List.of(
                        Category.builder().id(1L).name("CAFE").build(),
                        Category.builder().id(2L).name("BEBIDA").build()
                ));

        List<Category> result = categoryUseCase.findAll();
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Should delete category successfully")
    void shouldDeleteSuccessfully() {
        when(categoryRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> categoryUseCase.delete(1L));
        verify(categoryRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw when deleting non-existent category")
    void shouldThrowWhenDeletingNonExistent() {
        when(categoryRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> categoryUseCase.delete(99L));
    }
}
