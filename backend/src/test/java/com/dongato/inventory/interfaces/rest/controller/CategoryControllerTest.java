package com.dongato.inventory.interfaces.rest.controller;

import com.dongato.inventory.application.usecases.CategoryUseCase;
import com.dongato.inventory.domain.model.Category;
import com.dongato.inventory.interfaces.rest.dto.CategoryCreateDTO;
import com.dongato.inventory.interfaces.rest.dto.CategoryResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryUseCase categoryUseCase;

    @InjectMocks
    private CategoryController categoryController;

    private Category mockCategory() {
        return Category.builder().id(1L).name("Test Category").description("Test Desc").build();
    }

    @Test
    @DisplayName("Should create category and return 201")
    void shouldCreateCategory() {
        CategoryCreateDTO dto = new CategoryCreateDTO("Test Category", "Test Desc");
        when(categoryUseCase.create(any(Category.class))).thenReturn(mockCategory());

        ResponseEntity<CategoryResponseDTO> response = categoryController.create(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Category", response.getBody().getName());
    }

    @Test
    @DisplayName("Should find all categories and return 200")
    void shouldFindAll() {
        when(categoryUseCase.findAll()).thenReturn(List.of(mockCategory()));

        ResponseEntity<List<CategoryResponseDTO>> response = categoryController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("Should find category by id and return 200")
    void shouldFindById() {
        when(categoryUseCase.findById(1L)).thenReturn(mockCategory());

        ResponseEntity<CategoryResponseDTO> response = categoryController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    @DisplayName("Should update category and return 200")
    void shouldUpdateCategory() {
        CategoryCreateDTO dto = new CategoryCreateDTO("Updated Category", "Updated Desc");
        Category updated = mockCategory();
        updated.setName("Updated Category");
        updated.setDescription("Updated Desc");

        when(categoryUseCase.update(eq(1L), any(Category.class))).thenReturn(updated);

        ResponseEntity<CategoryResponseDTO> response = categoryController.update(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Category", response.getBody().getName());
    }

    @Test
    @DisplayName("Should delete category and return 204")
    void shouldDeleteCategory() {
        doNothing().when(categoryUseCase).delete(1L);

        ResponseEntity<Void> response = categoryController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(categoryUseCase).delete(1L);
    }
}
