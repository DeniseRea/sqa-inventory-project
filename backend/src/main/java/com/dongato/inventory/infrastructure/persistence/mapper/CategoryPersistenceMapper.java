package com.dongato.inventory.infrastructure.persistence.mapper;

import com.dongato.inventory.domain.model.Category;
import com.dongato.inventory.infrastructure.persistence.entity.CategoryEntity;

/**
 * Maps between Category domain model and CategoryEntity JPA entity.
 * <p>
 * McCall Factor: Maintainability — isolates domain from persistence representation.
 */
public final class CategoryPersistenceMapper {

    private CategoryPersistenceMapper() {
        // Utility class — prevent instantiation
    }

    public static Category toDomain(CategoryEntity entity) {
        if (entity == null) return null;
        return Category.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public static CategoryEntity toEntity(Category domain) {
        if (domain == null) return null;
        return CategoryEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .description(domain.getDescription())
                .createdAt(domain.getCreatedAt())
                .build();
    }
}
