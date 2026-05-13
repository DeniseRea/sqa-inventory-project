package com.dongato.inventory.interfaces.rest.mapper;

import com.dongato.inventory.domain.model.Category;
import com.dongato.inventory.interfaces.rest.dto.CategoryCreateDTO;
import com.dongato.inventory.interfaces.rest.dto.CategoryResponseDTO;

/**
 * Maps between Category domain model and REST DTOs.
 * McCall Factor: Maintainability — isolates API contract from domain.
 */
public final class CategoryRestMapper {

    private CategoryRestMapper() {
        // Utility class
    }

    public static Category toDomain(CategoryCreateDTO dto) {
        return Category.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    public static CategoryResponseDTO toResponseDto(Category domain) {
        return CategoryResponseDTO.builder()
                .id(domain.getId())
                .name(domain.getName())
                .description(domain.getDescription())
                .createdAt(domain.getCreatedAt())
                .build();
    }
}
