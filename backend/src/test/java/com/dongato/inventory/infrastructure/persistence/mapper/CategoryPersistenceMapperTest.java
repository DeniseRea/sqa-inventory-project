package com.dongato.inventory.infrastructure.persistence.mapper;

import com.dongato.inventory.domain.model.Category;
import com.dongato.inventory.infrastructure.persistence.entity.CategoryEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CategoryPersistenceMapperTest {

    @Test
    @DisplayName("Should map entity to domain")
    void shouldMapToDomain() {
        CategoryEntity entity = CategoryEntity.builder()
                .id(1L).name("Test").description("Test desc")
                .createdAt(LocalDateTime.now()).build();

        Category domain = CategoryPersistenceMapper.toDomain(entity);

        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getName(), domain.getName());
        assertEquals(entity.getDescription(), domain.getDescription());
        assertEquals(entity.getCreatedAt(), domain.getCreatedAt());
    }

    @Test
    @DisplayName("Should handle null entity")
    void shouldHandleNullEntity() {
        assertNull(CategoryPersistenceMapper.toDomain(null));
    }

    @Test
    @DisplayName("Should map domain to entity")
    void shouldMapToEntity() {
        Category domain = Category.builder()
                .id(1L).name("Test").description("Test desc")
                .createdAt(LocalDateTime.now()).build();

        CategoryEntity entity = CategoryPersistenceMapper.toEntity(domain);

        assertEquals(domain.getId(), entity.getId());
        assertEquals(domain.getName(), entity.getName());
        assertEquals(domain.getDescription(), entity.getDescription());
        assertEquals(domain.getCreatedAt(), entity.getCreatedAt());
    }

    @Test
    @DisplayName("Should handle null domain")
    void shouldHandleNullDomain() {
        assertNull(CategoryPersistenceMapper.toEntity(null));
    }
}
