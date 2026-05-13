package com.dongato.inventory.infrastructure.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CategoryEntityTest {

    @Test
    @DisplayName("Should set createdAt on persist when null")
    void shouldSetCreatedAtOnPersistWhenNull() {
        CategoryEntity entity = new CategoryEntity();

        entity.onCreate();

        assertNotNull(entity.getCreatedAt());
    }

    @Test
    @DisplayName("Should keep existing createdAt on persist")
    void shouldKeepExistingCreatedAtOnPersist() {
        LocalDateTime now = LocalDateTime.now().minusDays(1);
        CategoryEntity entity = new CategoryEntity();
        entity.setCreatedAt(now);

        entity.onCreate();

        assertEquals(now, entity.getCreatedAt());
    }
}
