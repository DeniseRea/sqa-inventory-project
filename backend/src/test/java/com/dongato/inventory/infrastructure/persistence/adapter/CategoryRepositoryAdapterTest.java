package com.dongato.inventory.infrastructure.persistence.adapter;

import com.dongato.inventory.domain.model.Category;
import com.dongato.inventory.infrastructure.persistence.entity.CategoryEntity;
import com.dongato.inventory.infrastructure.persistence.repository.JpaCategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryRepositoryAdapterTest {

    @Mock
    private JpaCategoryRepository repository;

    @InjectMocks
    private CategoryRepositoryAdapter adapter;

    private Category mockCategory() {
        return Category.builder().id(1L).name("Test").description("Test desc")
                .createdAt(LocalDateTime.now()).build();
    }

    private CategoryEntity mockEntity() {
        return CategoryEntity.builder().id(1L).name("Test").description("Test desc")
                .createdAt(LocalDateTime.now()).build();
    }

    @Test
    @DisplayName("Should save category")
    void shouldSaveCategory() {
        when(repository.save(any(CategoryEntity.class))).thenReturn(mockEntity());

        Category saved = adapter.save(mockCategory());

        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        verify(repository).save(any(CategoryEntity.class));
    }

    @Test
    @DisplayName("Should find category by id")
    void shouldFindById() {
        when(repository.findById(1L)).thenReturn(Optional.of(mockEntity()));

        Optional<Category> found = adapter.findById(1L);

        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
    }

    @Test
    @DisplayName("Should find all categories")
    void shouldFindAll() {
        when(repository.findAll()).thenReturn(List.of(mockEntity()));

        List<Category> categories = adapter.findAll();

        assertEquals(1, categories.size());
    }

    @Test
    @DisplayName("Should return true if category exists by id")
    void shouldReturnTrueIfExistsById() {
        when(repository.existsById(1L)).thenReturn(true);

        assertTrue(adapter.existsById(1L));
    }

    @Test
    @DisplayName("Should return true if category exists by name")
    void shouldReturnTrueIfExistsByName() {
        when(repository.existsByNameIgnoreCase("Test")).thenReturn(true);

        assertTrue(adapter.existsByName("Test"));
    }

    @Test
    @DisplayName("Should delete category by id")
    void shouldDeleteById() {
        doNothing().when(repository).deleteById(1L);

        adapter.deleteById(1L);

        verify(repository).deleteById(1L);
    }
}
