package com.dongato.inventory.infrastructure.persistence.mapper;

import com.dongato.inventory.domain.model.MovementReason;
import com.dongato.inventory.domain.model.MovementType;
import com.dongato.inventory.domain.model.StockMovement;
import com.dongato.inventory.infrastructure.persistence.entity.StockMovementEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StockMovementPersistenceMapperTest {

    @Test
    @DisplayName("Should map entity to domain")
    void shouldMapToDomain() {
        StockMovementEntity entity = StockMovementEntity.builder()
                .id(1L).productId(2L).type(MovementType.ENTRADA)
                .quantity(10).reason(MovementReason.REPOSICION)
                .createdAt(LocalDateTime.now()).build();

        StockMovement domain = StockMovementPersistenceMapper.toDomain(entity);

        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getProductId(), domain.getProductId());
        assertEquals(entity.getType(), domain.getType());
        assertEquals(entity.getQuantity(), domain.getQuantity());
        assertEquals(entity.getReason(), domain.getReason());
        assertEquals(entity.getCreatedAt(), domain.getCreatedAt());
    }

    @Test
    @DisplayName("Should handle null entity")
    void shouldHandleNullEntity() {
        assertNull(StockMovementPersistenceMapper.toDomain(null));
    }

    @Test
    @DisplayName("Should map domain to entity")
    void shouldMapToEntity() {
        StockMovement domain = StockMovement.builder()
                .id(1L).productId(2L).type(MovementType.ENTRADA)
                .quantity(10).reason(MovementReason.REPOSICION)
                .createdAt(LocalDateTime.now()).build();

        StockMovementEntity entity = StockMovementPersistenceMapper.toEntity(domain);

        assertEquals(domain.getId(), entity.getId());
        assertEquals(domain.getProductId(), entity.getProductId());
        assertEquals(domain.getType(), entity.getType());
        assertEquals(domain.getQuantity(), entity.getQuantity());
        assertEquals(domain.getReason(), entity.getReason());
        assertEquals(domain.getCreatedAt(), entity.getCreatedAt());
    }

    @Test
    @DisplayName("Should handle null domain")
    void shouldHandleNullDomain() {
        assertNull(StockMovementPersistenceMapper.toEntity(null));
    }
}
