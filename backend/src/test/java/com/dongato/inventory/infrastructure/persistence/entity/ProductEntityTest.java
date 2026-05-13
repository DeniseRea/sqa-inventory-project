package com.dongato.inventory.infrastructure.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductEntityTest {

    @Test
    @DisplayName("Should set createdAt on persist when null")
    void shouldSetCreatedAtOnPersistWhenNull() {
        ProductEntity entity = new ProductEntity();

        entity.onCreate();

        assertNotNull(entity.getCreatedAt());
    }

    @Test
    @DisplayName("Should keep existing createdAt on persist")
    void shouldKeepExistingCreatedAtOnPersist() {
        LocalDateTime now = LocalDateTime.of(2025, 2, 1, 9, 30);
        ProductEntity entity = new ProductEntity();
        entity.setCreatedAt(now);

        entity.onCreate();

        assertEquals(now, entity.getCreatedAt());
    }
}
