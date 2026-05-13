package com.dongato.inventory.infrastructure.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductEntityTest {

    @Test
    @DisplayName("Should set createdAt on persist when null")
    void shouldSetCreatedAtOnCreate() {
        ProductEntity entity = new ProductEntity();

        entity.onCreate();

        assertNotNull(entity.getCreatedAt());
    }

    @Test
    @DisplayName("Should keep existing createdAt on persist")
    void shouldKeepExistingCreatedAt() {
        LocalDateTime now = LocalDateTime.now().minusHours(2);
        ProductEntity entity = new ProductEntity();
        entity.setCreatedAt(now);

        entity.onCreate();

        assertEquals(now, entity.getCreatedAt());
    }
}
