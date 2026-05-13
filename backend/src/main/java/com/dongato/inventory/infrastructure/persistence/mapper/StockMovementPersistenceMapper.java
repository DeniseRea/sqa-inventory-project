package com.dongato.inventory.infrastructure.persistence.mapper;

import com.dongato.inventory.domain.model.StockMovement;
import com.dongato.inventory.infrastructure.persistence.entity.StockMovementEntity;

/**
 * Maps between StockMovement domain model and StockMovementEntity JPA entity.
 * <p>
 * McCall Factor: Maintainability — isolates audit data from persistence details.
 */
public final class StockMovementPersistenceMapper {

    private StockMovementPersistenceMapper() {
        // Utility class — prevent instantiation
    }

    public static StockMovement toDomain(StockMovementEntity entity) {
        if (entity == null) return null;
        return StockMovement.builder()
                .id(entity.getId())
                .productId(entity.getProductId())
                .type(entity.getType())
                .quantity(entity.getQuantity())
                .reason(entity.getReason())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public static StockMovementEntity toEntity(StockMovement domain) {
        if (domain == null) return null;
        return StockMovementEntity.builder()
                .id(domain.getId())
                .productId(domain.getProductId())
                .type(domain.getType())
                .quantity(domain.getQuantity())
                .reason(domain.getReason())
                .createdAt(domain.getCreatedAt())
                .build();
    }
}
