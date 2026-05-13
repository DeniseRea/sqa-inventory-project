package com.dongato.inventory.interfaces.rest.mapper;

import com.dongato.inventory.domain.model.MovementReason;
import com.dongato.inventory.domain.model.MovementType;
import com.dongato.inventory.domain.model.StockMovement;
import com.dongato.inventory.interfaces.rest.dto.StockMovementCreateDTO;
import com.dongato.inventory.interfaces.rest.dto.StockMovementResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StockMovementRestMapper.
 * McCall Factor: Maintainability — ensures correct field mapping between layers.
 */
class StockMovementRestMapperTest {

    @Test
    @DisplayName("Should map StockMovementCreateDTO to domain with all fields")
    void shouldMapCreateDtoToDomain() {
        StockMovementCreateDTO dto = StockMovementCreateDTO.builder()
                .productId(1L)
                .type(MovementType.ENTRADA)
                .quantity(10)
                .reason(MovementReason.REPOSICION)
                .build();

        StockMovement movement = StockMovementRestMapper.toDomain(dto);

        assertEquals(1L, movement.getProductId());
        assertEquals(MovementType.ENTRADA, movement.getType());
        assertEquals(10, movement.getQuantity());
        assertEquals(MovementReason.REPOSICION, movement.getReason());
    }

    @Test
    @DisplayName("Should map StockMovementCreateDTO with SALIDA type to domain")
    void shouldMapSalidaDtoToDomain() {
        StockMovementCreateDTO dto = StockMovementCreateDTO.builder()
                .productId(2L)
                .type(MovementType.SALIDA)
                .quantity(3)
                .reason(MovementReason.VENTA)
                .build();

        StockMovement movement = StockMovementRestMapper.toDomain(dto);

        assertEquals(MovementType.SALIDA, movement.getType());
        assertEquals(MovementReason.VENTA, movement.getReason());
    }

    @Test
    @DisplayName("Should map StockMovement domain to StockMovementResponseDTO with all fields")
    void shouldMapDomainToResponseDto() {
        LocalDateTime now = LocalDateTime.now();
        StockMovement domain = StockMovement.builder()
                .id(5L).productId(1L)
                .type(MovementType.ENTRADA).quantity(20)
                .reason(MovementReason.REPOSICION)
                .createdAt(now).build();

        StockMovementResponseDTO dto = StockMovementRestMapper.toResponseDto(domain);

        assertEquals(5L, dto.getId());
        assertEquals(1L, dto.getProductId());
        assertEquals(MovementType.ENTRADA, dto.getType());
        assertEquals(20, dto.getQuantity());
        assertEquals(MovementReason.REPOSICION, dto.getReason());
        assertEquals(now, dto.getCreatedAt());
    }
}
