package com.dongato.inventory.infrastructure.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StockMovementEntityTest {

    @Test
    @DisplayName("Should set createdAt on persist when null")
    void shouldSetCreatedAtOnCreate() {
        StockMovementEntity entity = new StockMovementEntity();

        entity.onCreate();

        assertNotNull(entity.getCreatedAt());
    }

    @Test
    @DisplayName("Should keep existing createdAt on persist")
    void shouldKeepExistingCreatedAt() {
        LocalDateTime now = LocalDateTime.now().minusMinutes(30);
        StockMovementEntity entity = new StockMovementEntity();
        entity.setCreatedAt(now);

        entity.onCreate();

        assertEquals(now, entity.getCreatedAt());
    }
}
