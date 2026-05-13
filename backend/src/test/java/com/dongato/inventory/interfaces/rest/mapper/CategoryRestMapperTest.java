package com.dongato.inventory.interfaces.rest.mapper;

import com.dongato.inventory.domain.model.Category;
import com.dongato.inventory.interfaces.rest.dto.CategoryCreateDTO;
import com.dongato.inventory.interfaces.rest.dto.CategoryResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CategoryRestMapper.
 * McCall Factor: Maintainability — ensures correct field mapping between layers.
 */
class CategoryRestMapperTest {

    @Test
    @DisplayName("Should map CategoryCreateDTO to domain")
    void shouldMapCreateDtoToDomain() {
        CategoryCreateDTO dto = CategoryCreateDTO.builder()
                .name("CAFE")
                .description("Cafés calientes")
                .build();

        Category category = CategoryRestMapper.toDomain(dto);

        assertEquals("CAFE", category.getName());
        assertEquals("Cafés calientes", category.getDescription());
    }

    @Test
    @DisplayName("Should map Category domain to CategoryResponseDTO with all fields")
    void shouldMapDomainToResponseDto() {
        LocalDateTime now = LocalDateTime.now();
        Category domain = Category.builder()
                .id(1L).name("BEBIDA")
                .description("Bebidas frías")
                .createdAt(now).build();

        CategoryResponseDTO dto = CategoryRestMapper.toResponseDto(domain);

        assertEquals(1L, dto.getId());
        assertEquals("BEBIDA", dto.getName());
        assertEquals("Bebidas frías", dto.getDescription());
        assertEquals(now, dto.getCreatedAt());
    }

    @Test
    @DisplayName("Should map CategoryCreateDTO with null description to domain")
    void shouldMapCreateDtoWithNullDescription() {
        CategoryCreateDTO dto = CategoryCreateDTO.builder()
                .name("SNACK")
                .description(null)
                .build();

        Category category = CategoryRestMapper.toDomain(dto);

        assertEquals("SNACK", category.getName());
        assertNull(category.getDescription());
    }
}
