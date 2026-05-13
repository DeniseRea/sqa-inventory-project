package com.dongato.inventory.interfaces.rest.mapper;

import com.dongato.inventory.domain.model.StockMovement;
import com.dongato.inventory.interfaces.rest.dto.StockMovementCreateDTO;
import com.dongato.inventory.interfaces.rest.dto.StockMovementResponseDTO;

/**
 * Maps between StockMovement domain model and REST DTOs.
 * McCall Factor: Maintainability — isolates API contract from domain.
 */
public final class StockMovementRestMapper {

    private StockMovementRestMapper() {
        // Utility class
    }

    public static StockMovement toDomain(StockMovementCreateDTO dto) {
        return StockMovement.builder()
                .productId(dto.getProductId())
                .type(dto.getType())
                .quantity(dto.getQuantity())
                .reason(dto.getReason())
                .build();
    }

    public static StockMovementResponseDTO toResponseDto(StockMovement domain) {
        return StockMovementResponseDTO.builder()
                .id(domain.getId())
                .productId(domain.getProductId())
                .type(domain.getType())
                .quantity(domain.getQuantity())
                .reason(domain.getReason())
                .createdAt(domain.getCreatedAt())
                .build();
    }
}
